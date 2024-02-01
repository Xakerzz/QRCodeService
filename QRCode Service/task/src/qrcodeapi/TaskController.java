package qrcodeapi;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L;

@RestController
public class TaskController {


    @GetMapping("/api/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam("contents") String contents,
                                                 @RequestParam(value = "size", defaultValue = "250") int size,
                                                 @RequestParam(value = "type", defaultValue = "png") String type,
                                                 @RequestParam(value = "correction", defaultValue = "L") String correction) {

        if (contents == null || contents.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Contents cannot be null or blank\"}".getBytes());
        } else if (size < 150 || size > 350) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"Image size must be between 150 and 350 pixels\"}"
                            .getBytes());
        }else if (!isValidCorrection(correction)) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"Permitted error correction levels are L, M, Q, H\"}"
                            .getBytes());
        } else if (!type.equalsIgnoreCase("png") &&
                !type.equalsIgnoreCase("jpeg") &&
                !type.equalsIgnoreCase("gif")) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"Only png, jpeg and gif image types are supported\"}"
                            .getBytes());
        }  else {

            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, getErrorCorrectionLevel(correction));

            try {
                BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, type, baos);
                baos.flush();
                byte[] imageBytes = baos.toByteArray();
                baos.close();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(getMediaType(type));

                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

            } catch (WriterException | IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    private boolean isValidCorrection(String correction) {
        return correction.equalsIgnoreCase("L") || correction.equalsIgnoreCase("M") ||
                correction.equalsIgnoreCase("Q") || correction.equalsIgnoreCase("H");
    }

    private ErrorCorrectionLevel getErrorCorrectionLevel(String correction) {
        switch (correction.toUpperCase()) {
            case "M": return ErrorCorrectionLevel.M;
            case "Q": return ErrorCorrectionLevel.Q;
            case "H": return ErrorCorrectionLevel.H;
            default: return ErrorCorrectionLevel.L;
        }
    }

    private MediaType getMediaType(String type) {
        if (type.equalsIgnoreCase("png")) {
            return MediaType.IMAGE_PNG;
        } else if (type.equalsIgnoreCase("jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (type.equalsIgnoreCase("gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }


}
