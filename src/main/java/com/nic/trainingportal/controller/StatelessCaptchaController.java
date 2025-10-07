package com.nic.trainingportal.controller;

import com.nic.trainingportal.utility.Utility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class StatelessCaptchaController {


    @GetMapping("/captcha")
    public CaptchaResponse getCaptcha() throws Exception {
        String captchaText = generateCaptchaText(6);
        System.out.println(captchaText);
        String token = Utility.createCaptchaToken(captchaText);
        String base64Image = generateBase64CaptchaImage(captchaText);
        return new CaptchaResponse(base64Image, token);
    }

//    @PostMapping("/verify-captcha")
//    public String verifyCaptcha(@RequestBody CaptchaVerifyRequest request) {
//        String actualCaptcha = Utility.getCaptchaFromToken(request.getCaptchaToken());
//
//        if (actualCaptcha == null) {
//            return "CAPTCHA Expired or Invalid";
//        }
//
//        return actualCaptcha.equalsIgnoreCase(request.getUserCaptcha())
//                ? "CAPTCHA Verified"
//                : "Invalid CAPTCHA";
//    }


    private String generateCaptchaText(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";

        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String generateBase64CaptchaImage(String captchaText) throws Exception {
        int width = 200;
        int height = 70;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Random random = new Random();

        // Draw characters with random position, angle, and font
        for (int i = 0; i < captchaText.length(); i++) {
            char ch = captchaText.charAt(i);

            // Random color
            g2d.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));

            // Random font
            int fontSize = 36 + random.nextInt(10);
            Font font = new Font("Arial", Font.BOLD | Font.ITALIC, fontSize);
            g2d.setFont(font);

            // Random rotation angle
            double rotation = (random.nextInt(60) - 30) * Math.PI / 180; // -30° to +30°

            // Position
            int x = 25 + i * 30 + random.nextInt(10); // spread out + jitter
            int y = 40 + random.nextInt(20); // vertical randomness

            // Rotate and draw character
            g2d.translate(x, y);
            g2d.rotate(rotation);
            g2d.drawString(String.valueOf(ch), 0, 0);
            g2d.rotate(-rotation);
            g2d.translate(-x, -y);
        }

        // Optional: draw interference lines
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }

        g2d.dispose();

        // Convert image to Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }


    static class CaptchaResponse {
        public String imageBase64;
        public String captchaToken;

        public CaptchaResponse(String imageBase64, String captchaToken) {
            this.imageBase64 = imageBase64;
            this.captchaToken = captchaToken;
        }
    }

    static class CaptchaVerifyRequest {
        public String userCaptcha;
        public String captchaToken;

        public String getUserCaptcha() {
            return userCaptcha;
        }

        public String getCaptchaToken() {
            return captchaToken;
        }
    }
}

