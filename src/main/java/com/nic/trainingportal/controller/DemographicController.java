package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.DemographicDao;
import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.DemographicService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")

public class DemographicController {
    /**
     * create logger class object
     */
    private static final Logger logger = LoggerFactory.getLogger(DemographicController.class);
    @Autowired
    public DemographicService demographicservice;
    @Autowired
    private HttpServletRequest httpservletrequest;

    @Autowired
    public Utility utility;

    @Autowired
    private JWT jwt;

    @Autowired
    private DemographicDao demographic;

    /**
     * Add Demographic Details
     *
     * @param map
     * @return
     */
    @PostMapping(value = Webhook.addDemographicDetails)
    public Map<String, Object> addFaculty(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
//        /**
//         *  check token is valid or not
//         */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
//        int userId=demographic.getUserId(map);

            if (demographic.exist(map)) {
                dataMap.put(Literal.message, "already created");
                return dataMap;
            }
            /**
             * check Null
             */
            if (Utility.checkNull(map.get("totalPopulation"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Total Population");
                return dataMap;
            }
            /**
             * check Null
             */
            if (Utility.checkNull(map.get("totalRuralPopulation"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Total Rural Population");
                return dataMap;
            }

            /**
             * check Null
             */
            if (Utility.checkNull(map.get("percentOfRural"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Percent OF Rural");
                return dataMap;
            }try {
                int totalPopulation = Integer.parseInt(map.get("totalPopulation").toString().trim());
                int totalRuralPopulation = Integer.parseInt(map.get("totalRuralPopulation").toString().trim());
                double percentOfRural = Double.parseDouble(map.get("percentOfRural").toString().trim());

                if (totalPopulation <= 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Total Population must be greater than 0");
                    return dataMap;
                }

                if (totalRuralPopulation < 0 || totalRuralPopulation > totalPopulation) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Total Rural Population must be between 0 and Total Population");
                    return dataMap;
                }

                double expectedPercent = (totalRuralPopulation * 100.0) / totalPopulation;
                if (Math.abs(expectedPercent - percentOfRural) > 0.5) { // small margin allowed
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Percent of Rural Population does not match the values provided");
                    return dataMap;
                }

            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Population fields must be valid numbers");
                return dataMap;
            }
            /**
             * check Null
             */
            if (Utility.checkNull(map.get("stateCode"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide  State Code value");
                return dataMap;
            }
            /**
             * Print log
             */
            logger.info("Add Demographic Request Data", map);

            dataMap.put(Literal.status, Literal.successCode);
           int result= demographicservice.addDemographicDetails(map);
            dataMap.put(Literal.statusCode, result);

            if (result == 0) {
                dataMap.put(Literal.message, "Record not inserted");
            } else {
                dataMap.put(Literal.message, "Record inserted successfully");
            }
            dataMap.remove(Literal.statusCode);
            return dataMap;

        }catch (Exception e) {
            /**
             * print error log
             */
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something went wrong");
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
            return dataMap;
        }

    }

    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = Webhook.getDemographicDetails)
    public Map<String, Object> getDemographicDetails( @RequestHeader String token,@RequestBody Map<String, Object> maps) {
        Map<String, Object> map = new HashMap<String, Object>(4);
        try {
            /**
             *  check token is valid or not
             //			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                map.put(Literal.status, Literal.unauthorized);
                map.put(Literal.message, "Your Token Is Expired");
                return map;
            }

            String userName = jwt.extractUsername(token);

            map.put(Literal.status, Literal.successCode);
            int userId = demographic.getUserId(userName, (String) maps.get("userType"));
            map.put(Literal.data, demographicservice.getDemographicDetails((int)maps.get("pageSize"),(int)maps.get("pageNumber") , userId,(String) maps.get("userType"), userName));
            map.put("totalCount", demographic.getDemographicCount(userId, (String) maps.get("userType")));
            /**
             * Print log
             */
            logger.info("Get Demographic Details", map);
            return map;

        } catch (Exception e) {
            map.put(Literal.status, Literal.errorCode);
            map.put(Literal.message, "Something Went Wrong");
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
            return map;
        }

    }

    /**
     * Add Demographic Details
     *
     * @param map
     * @return
     */
    @PostMapping(value = Webhook.updateDemographicDetails)
    public Map<String, Object> updateDemographicDetails(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {

            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                map.put(Literal.status, Literal.unauthorized);
                map.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            /**
             * check Null
             */
            if (Utility.checkNull(map.get("totalPopulation"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Total Population");
                return dataMap;
            }
            /**
             * check Null
             */
            if (Utility.checkNull(map.get("totalRuralPopulation"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Total Rural Population");
                return dataMap;
            }

            /**
             * check Null
             */
            if (Utility.checkNull(map.get("percentOfRural"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Percent OF Rural");
                return dataMap;
            }try {
                int totalPopulation = Integer.parseInt(map.get("totalPopulation").toString().trim());
                int totalRuralPopulation = Integer.parseInt(map.get("totalRuralPopulation").toString().trim());
                double percentOfRural = Double.parseDouble(map.get("percentOfRural").toString().trim());

                if (totalPopulation <= 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Total Population must be greater than 0");
                    return dataMap;
                }

                if (totalRuralPopulation < 0 || totalRuralPopulation > totalPopulation) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Total Rural Population must be between 0 and Total Population");
                    return dataMap;
                }

                double expectedPercent = (totalRuralPopulation * 100.0) / totalPopulation;
                if (Math.abs(expectedPercent - percentOfRural) > 0.5) { // small margin allowed
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Percent of Rural Population does not match the values provided");
                    return dataMap;
                }

            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Population fields must be valid numbers");
                return dataMap;
            }
            /**
             * check Null
             */
            if (Utility.checkNull(map.get("stateCode"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide  State Code value");
                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.statusCode, demographicservice.updateDemographicDetails(map));
            dataMap.put(Literal.message, "Record Updated Successfully");
            dataMap.remove(Literal.statusCode);
            return dataMap;

        }catch (Exception e) {
            /**
             * print error log
             */
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
            return dataMap;
        }
    }


    @PostMapping(value = Webhook.getDemographicById)

    public Map<String, Object> getDemographicById(@RequestBody Map<String,Object> maps) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.getDemographicById((maps.get("id").toString())));
            /**
             * print log
             */
            logger.info("Get All Faculty", dataMap);
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @PostMapping (value = Webhook.deleteDemographicById)

    public Map<String, Object> deleteDemographicById(@RequestBody Map<String,Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.deleteDemographicById(map.get("id").toString()));
            /**
             * print log
             */
            logger.info("Get All Faculty", dataMap);
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    @GetMapping(value = Webhook.getFinancialYear)
    public Map<String, Object> getFinancialYear() {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("Data", demographicservice.getFinnancialYear());
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    @PostMapping(value = "/upload")
    public Map<String, Object> uploadPDF(MultipartFile file, String newFileName) {
        String directoryPath = "E:\\AreaOfficer_PDF";
//        String directoryPath = "C:\\Users\\hp\\Pictures";
        Map<String, Object> dataMap = new HashMap<>();

        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            // Validate file extension
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!fileExtension.equalsIgnoreCase("pdf")) {
                dataMap.put("status", "error");
                dataMap.put("message", "File type not allowed: Only .pdf files are accepted.");
                return dataMap;
            }

            // Validate content type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equalsIgnoreCase("application/pdf")) {
                dataMap.put("status", "error");
                dataMap.put("message", "Invalid content type: Only PDF files are allowed.");
                return dataMap;
            }

            // Validate file name (prevent directory traversal and allow only safe characters)
            if (newFileName.contains("..") || !newFileName.matches("^[a-zA-Z0-9_\\-]+$")) {
                dataMap.put("status", "error");
                dataMap.put("message", "Invalid file name: only alphanumeric, underscores and dashes are allowed.");
                return dataMap;
            }

            // Validate actual file content using magic number
            if (!isValidPDF(new BufferedInputStream(file.getInputStream()))) {
                dataMap.put("status", "error");
                dataMap.put("message", "Uploaded file is not a valid PDF.");
                return dataMap;
            }

            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(directoryPath);
            // Ensure the upload directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Define the target file path with the new file name
//        Path filePath = uploadPath.resolve(newFileName+"_"+userName+"_"+currentDate +".pdf");

            Path filePath = uploadPath.resolve(newFileName+".pdf");

            // Save the file (overwrite if it already exists)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            dataMap.put("status", "success");
            dataMap.put("message", "File uploaded successfully.");
            dataMap.put("fileName", newFileName);
            return dataMap;

        } catch (IOException e) {
            e.printStackTrace();
            dataMap.put("status", "error");
            dataMap.put("message", "File not uploaded.");
            dataMap.put("errorMessage", e.getMessage());
            return dataMap;
        }
    }


    private boolean isValidPDF(InputStream inputStream) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            bis.mark(4);
            byte[] header = new byte[4];
            int read = bis.read(header, 0, 4);
            bis.reset();

            if (read == 4) {
                String headerStr = new String(header);
                return headerStr.equals("%PDF");
            }
            return false;
        }
    }



    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }



    @PostMapping (value = "file-path")
    public Map<String, Object> getFilePath(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String fileName = requestBody.get("fileName");
            String date = requestBody.get("date");
            String userName = requestBody.get("userName");

//            String directoryPath = "E:\\AreaOfficer_PDF";
            String directoryPath = "C://Users/hp/Pictures/";


            Path filePath = Paths.get(directoryPath, fileName + "_" + userName + "_" + date);
            String fileNameNew = filePath.getFileName().toString();
            dataMap.put(Literal.status, Literal.successCode);
//            dataMap.put("path", "https://mordtrainingportal.nic.in/AreaOfficer_PDF/" + fileNameNew + ".pdf");
            dataMap.put("path", "C://Users/hp/Pictures" + fileNameNew + ".pdf");
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put("errorMessage", e.getMessage());
            return dataMap;

        }
    }

    @GetMapping(value = "GetProposalInstallment")
    public Map<String, Object> GetProposalInstallment(@RequestParam String userType,@RequestHeader String token, @RequestParam String financialYear) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            String userName = jwt.extractUsername(token);

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("Data", demographicservice.GetProposalInstallment(userType, userName, financialYear));
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    @GetMapping(value = "GetStateCode")
    public Map<String, Object> GetStateCode(@RequestParam String userType, @RequestHeader String token) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            String userName = jwt.extractUsername(token);

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("stateCode", demographicservice.GetStateCode(userType, userName));
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @GetMapping(value = "GetProposalInstallmentNonRecurring")
    public Map<String, Object> GetProposalInstallmentNonRecurring(@RequestParam String userType, @RequestHeader String token) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            String userName = jwt.extractUsername(token);

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("Data", demographicservice.GetProposalInstallmentNonRecurring(userType, userName));
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    @GetMapping(value = "getDemographicFinal")
    public Map<String, Object> getDemographicFinal() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.getDemographicFinal());
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put("errorMessage", e.getMessage());
            return dataMap;
        }

        return dataMap;

    }

    @GetMapping(value = "getProposalVsApproval")
    public Map<String, Object> getProposalVsApproval() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.getProposalVsApproval());
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put("errorMessage", e.getMessage());
            return dataMap;
        }

        return dataMap;

    }


    @GetMapping(value = "sirdSanction")
    public Map<String, Object> sirdSanction() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.sirdSanction());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return dataMap;
    }

    @GetMapping(value = "etcSanction")
    public Map<String, Object> etcSanction() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.etcSanction());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return dataMap;
    }

    @GetMapping(value = "proVsAppCount")
    public Map<String, Object> proVsAppCount() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.proVsAppCount());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return dataMap;
    }

    @GetMapping(value = "proposalApproval")
    public Map<String, Object> proposalApproval() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, demographicservice.proposalApproval());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return dataMap;
    }

    @PostMapping(value = "updateDemographicProposal")
    public Map<String, Object> updateDemographicProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.message, demographicservice.updateDemographicProposal(map));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return dataMap;
    }

    @GetMapping(value = "getRemarksReport")
    public Map<String, Object> remarksReport(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.message, demographicservice.remarksReport(map));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return dataMap;
    }

}
