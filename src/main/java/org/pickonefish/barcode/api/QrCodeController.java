package org.pickonefish.barcode.api;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
public class QrCodeController {

  @GetMapping(value = "/" + "qr-code")
  public ResponseEntity<?> qrCode(@RequestHeader(value = "User-Agent") String userAgent) throws Exception {
    BufferedImage image = generateQRCodeImage("");
    FileOutputStream output = new FileOutputStream("qr-code.png");
    ImageIO.write(image, "PNG", output);

    Path path = Paths.get("./").resolve("qr-code.png");
    Resource file = new UrlResource(path.toUri());
    return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG.getType())
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qr-code.png\"")
            .body(file);
  }

  public BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
    QRCodeWriter barcodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 400, 400);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }
}
