package tn.esprit.projet4arcticback.user_service.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.projet4arcticback.user_service.entity.User;
import tn.esprit.projet4arcticback.user_service.repository.UserRepository;
import tn.esprit.projet4arcticback.user_service.service.JwtService;
import tn.esprit.projet4arcticback.user_service.service.UserService;
import tn.esprit.projet4arcticback.user_service.utilities.MultipartInputStreamFileResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/facial")
@CrossOrigin(origins = "*")
public class FacialRecognitionController {

    private static final Logger logger = LoggerFactory.getLogger(FacialRecognitionController.class);
    private static final String UPLOAD_DIR = "user-service/src/main/resources/static/images/";
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtUtil;
    @Autowired
    private UserRepository userRepository;

  /*  @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file1") MultipartFile file, @RequestParam("id") Long id) {
        logger.info("Entering uploadImage endpoint - File: {}, User ID: {}", file.getOriginalFilename(), id);

        try {
            logger.debug("Attempting to retrieve user with ID: {}", id);
            User user = userService.getUserById(id);

            if (user == null) {
                logger.error("User not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found with ID: " + id));
            }

            String fileName = user.getName() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            logger.debug("Preparing to save file to: {}", path);

            // Create directory if it doesn't exist
            Files.createDirectories(path.getParent());
            logger.debug("Created directories if they didn't exist");

            // Save the file
            Files.write(path, file.getBytes());
            logger.info("Successfully saved image file: {}", fileName);

            // Update user with image filename
            user.setImage(fileName);
            userService.modifyUser(user);
            logger.debug("Updated user {} with image filename: {}", user.getIdUser(), fileName);

            rechargerImages();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Image saved as: " + fileName);
            logger.info("Upload completed successfully for user ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error processing file upload for user ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }*/
  @PostMapping("/upload")
  public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file1") MultipartFile file, @RequestParam("id") Long id) {
      logger.info("Entering uploadImage endpoint - File: {}, User ID: {}", file.getOriginalFilename(), id);

      try {
          logger.debug("Attempting to retrieve user with ID: {}", id);
          User user = userService.getUserById(id);

          if (user == null) {
              logger.error("User not found with ID: {}", id);
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .body(Map.of("error", "User not found with ID: " + id));
          }

          String fileName = user.getName() + "_" + file.getOriginalFilename();
          Path path = Paths.get(UPLOAD_DIR + fileName);
          logger.debug("Preparing to save file to: {}", path);

          // Create directory if it doesn't exist
          Files.createDirectories(path.getParent());
          logger.debug("Created directories if they didn't exist");

          // Save the file
          Files.write(path, file.getBytes());
          logger.info("Successfully saved image file: {}", fileName);

          // Update user with image filename
          user.setImage(fileName);
          userService.modifyUser(user);
          logger.debug("Updated user {} with image filename: {}", user.getIdUser(), fileName);

          rechargerImages();

          Map<String, String> response = new HashMap<>();
          response.put("message", "Image saved as: " + fileName);
          logger.info("Upload completed successfully for user ID: {}", id);
          return ResponseEntity.ok(response);
      } catch (IOException e) {
          logger.error("Error processing file upload for user ID: {}", id, e);
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(Map.of("error", "Error: " + e.getMessage()));
      }
  }


    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody Map<String, String> payload) {
        logger.info("Entering verify endpoint");

        try {
            logger.debug("Extracting base64 image from payload");
            String base64Image = payload.get("image");

            if (base64Image == null || base64Image.isEmpty()) {
                logger.error("No image data found in payload");
                return ResponseEntity.badRequest().body("No image data provided");
            }

            // Remove prefix "data:image/png;base64,"
            String[] parts = base64Image.split(",");
            String imageData = parts.length > 1 ? parts[1] : parts[0];
            logger.debug("Processed base64 image data");

            byte[] imageBytes = Base64.getDecoder().decode(imageData);
            logger.debug("Decoded base64 image to bytes");

            // Temporary save of the image
            Path tempFile = Files.createTempFile("webcam-", ".png");
            Files.write(tempFile, imageBytes);
            logger.info("Temporarily saved image to: {}", tempFile.toAbsolutePath());

            // TODO: Add facial recognition service call here
            logger.debug("Placeholder for facial recognition service call");

            return ResponseEntity.ok("User recognized ✅");
        } catch (Exception e) {
            logger.error("Error in verify endpoint", e);
            return ResponseEntity.status(500).body("Error during processing ❌");
        }
    }

    /*@PostMapping("/compare-faces")
    public ResponseEntity<?> compareFaces(
            @RequestParam("file1") MultipartFile file
    ) {
        logger.info("Entering compareFaces endpoint - File: {}", file.getOriginalFilename());
        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                logger.error("Received empty file in compareFaces");
                return ResponseEntity.badRequest().body("File is empty");
            }

            logger.debug("Retrieving all users from database");
            List<User> listUser = userService.getAllUsers();
            List<User> listUserfiltrer = new ArrayList<>();

            logger.debug("Filtering users with images");
            for (User user : listUser) {
                if (user.getImage() != null) {
                    listUserfiltrer.add(user);
                }
            }

            logger.info("Found {} users with images", listUserfiltrer.size());
            if (listUserfiltrer.isEmpty()) {
                logger.warn("No users with images found in database");
                response.put("message", "No registered users with images found");
                return ResponseEntity.ok(response);
            }

            for (User user : listUserfiltrer) {
                logger.debug("Processing user: {} with image: {}", user.getName(), user.getImage());

                // Prepare dynamic image (sent from Angular)
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                logger.debug("Set headers for multipart form data");

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("file1", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
                logger.debug("Added dynamic image to request body");

                // Load static image from filesystem

                String imagePath = "C:/Users/bekir/OneDrive/Bureau/Nouveau dossier/GeniusBackend/src/main/resources/static/images/" + user.getImage();
                logger.debug("Looking for user image at: {}", imagePath);

                File staticFile = new File(imagePath);
                if (staticFile.exists()) {
                    logger.debug("Found user image file");
                    body.add("file2", new FileSystemResource(staticFile));

                    // Create request
                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                    String pythonApiUrl = "http://127.0.0.1:8000/compare-faces/";
                    logger.debug("Preparing request to Python API at: {}", pythonApiUrl);

                    RestTemplate restTemplate = new RestTemplate();
                    logger.debug("Sending request to facial recognition service");
                    ResponseEntity<String> pythonResponse = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

                    logger.debug("Received response from facial recognition service: {}", pythonResponse.getBody());

                    // Parse the JSON response
                    ObjectMapper objectMapper = new ObjectMapper();
                    FaceMatchResponse responseF = objectMapper.readValue(pythonResponse.getBody(), FaceMatchResponse.class);

                    if (responseF.isMatch()) {
                        logger.info("Facial match found for user: {}", user.getName());

                        // Generate JWT token
                        var claims = new HashMap<String, Object>();

                        claims.put("fullName", user.fullName());
                        claims.put("role", user.getRoles().name());
                        String token = jwtUtil.generateToken2(claims, user);
                        logger.debug("Generated JWT token for user: {}", user.getName());

                        return ResponseEntity.ok(new AuthentificationResponse(token));
                    } else {
                        logger.debug("No match found for user: {}", user.getName());
                    }
                } else {
                    logger.warn("Image file not found for user: {} at path: {}", user.getName(), imagePath);
                }
            }

            logger.info("No facial matches found for any registered users");
            response.put("message", "No matching faces found");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error in compareFaces endpoint", e);
            response.put("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/

    @PostMapping(value = "/compare-faces", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> compareFaces(@RequestPart("file1") MultipartFile file,
                                          Authentication authentication) {
        logger.info("Entering compareFaces endpoint - File: {}", file.getOriginalFilename());
        Map<String, Object> response = new HashMap<>();

        ;
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String email = authentication.getName(); // Usually the username/email

        try {
            if (file.isEmpty()) {
                logger.error("Received empty file in compareFaces");
                return ResponseEntity.badRequest().body("File is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                logger.error("Invalid file type: {}", contentType);
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // Fetch the logged-in user from the database
            User user = userRepository.findUserByEmail(email).orElseThrow();
            if (user == null || user.getImage() == null) {
                logger.warn("User not found or has no image: {}", email);
                response.put("message", "No image found for logged-in user");
                return ResponseEntity.ok(response);
            }

            logger.info("Processing image comparison for user: {}", user.getName());

            // Prepare image comparison request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file1", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            String imagePath = "user-service/src/main/resources/static/images/" + user.getImage();

            File staticFile = new File(imagePath);
            if (!staticFile.exists()) {
                logger.warn("Stored image not found at path: {}", user.getImage());
                response.put("message", "Stored image file not found");
                return ResponseEntity.ok(response);
            }

            body.add("file2", new FileSystemResource(staticFile));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String pythonApiUrl = "http://127.0.0.1:8000/compare-faces/";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> pythonResponse = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            FaceMatchResponse responseF = objectMapper.readValue(pythonResponse.getBody(), FaceMatchResponse.class);

            if (responseF.isMatch()) {
                logger.info("Facial match confirmed for user: {}", user.getName());

                var claims = new HashMap<String, Object>();
                claims.put("id", user.getIdUser());
                claims.put("fullName", user.fullName());
                claims.put("role", user.getRoles().name());
                claims.put("imageVerified",true);
                String token = jwtUtil.generateToken2(claims, user);

                return ResponseEntity.ok(new AuthentificationResponse(token, user.getIdUser(), user.getEmail()));
            } else {
                logger.info("Facial match failed for user: {}", user.getName());
                response.put("message", "Face does not match");
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            logger.error("Error in compareFaces endpoint", e);
            response.put("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    public void rechargerImages() throws IOException {
        logger.info("Starting image reload process");

        Path source = Paths.get("user-service/src/main/resources/static/images/");
        Path destination = Paths.get("user-service/target/classes/static/images/");

        logger.debug("Source directory: {}", source);
        logger.debug("Destination directory: {}", destination);

        Files.walk(source)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        Path relativePath = source.relativize(file);
                        Path destFile = destination.resolve(relativePath);

                        logger.debug("Copying file from {} to {}", file, destFile);

                        Files.createDirectories(destFile.getParent());
                        Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);

                        logger.info("Successfully copied image: {}", file.getFileName());
                    } catch (IOException e) {
                        logger.error("Error copying file: {}", file.getFileName(), e);
                    }
                });

        logger.info("Completed image reload process");
    }
}