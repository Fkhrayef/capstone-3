package com.fkhrayef.capstone3.Service;

import com.adobe.pdfservices.operation.PDFServices;
import com.adobe.pdfservices.operation.PDFServicesMediaType;
import com.adobe.pdfservices.operation.PDFServicesResponse;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.auth.ServicePrincipalCredentials;
import com.adobe.pdfservices.operation.io.Asset;
import com.adobe.pdfservices.operation.io.StreamAsset;
import com.adobe.pdfservices.operation.pdfjobs.jobs.DocumentMergeJob;
import com.adobe.pdfservices.operation.pdfjobs.params.documentmerge.DocumentMergeParams;
import com.adobe.pdfservices.operation.pdfjobs.params.documentmerge.OutputFormat;
import com.adobe.pdfservices.operation.pdfjobs.result.DocumentMergeResult;
import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.DTOout.ContractDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileService {

    private final S3Service s3Service;

    private PDFServices pdfServices;
    @Value("${PDF_SERVICES_CLIENT_ID}")
    private String PDF_ID;
    @Value("${PDF_SERVICES_CLIENT_SECRET}")
    private String PDF_SECRET;

    @PostConstruct
    public void initialize() {
        Credentials credentials = new ServicePrincipalCredentials(PDF_ID, PDF_SECRET);
        pdfServices = new PDFServices(credentials);
    }

    public void createContract(ContractDTO contractDTO, String contractPath) throws ApiException {
        InputStream inputStream;
        Asset asset;
        JSONObject jsonDataForMerge;
        try {
            inputStream = new File("src/main/resources/" + contractPath + ".docx").toURI().toURL().openStream();
            asset = pdfServices.upload(inputStream, PDFServicesMediaType.DOCX.getMediaType());
            jsonDataForMerge = new JSONObject(contractDTO);
        } catch (Exception e) {
            throw new ApiException("Could not create contract file");
        }

        DocumentMergeParams documentMergeParams = DocumentMergeParams.documentMergeParamsBuilder()
                .withJsonDataForMerge(jsonDataForMerge)
                .withOutputFormat(OutputFormat.PDF)
                .build();

        DocumentMergeJob documentMergeJob = new DocumentMergeJob(asset, documentMergeParams);

        try {

            String location = pdfServices.submit(documentMergeJob);
            PDFServicesResponse<DocumentMergeResult> pdfServicesResponse = pdfServices.getJobResult(location, DocumentMergeResult.class);


            Asset resultAsset = pdfServicesResponse.getResult().getAsset();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);

            byte[] fileContent = IOUtils.toByteArray(streamAsset.getInputStream());

            String s3Key = contractPath + "_" + contractDTO.getStartup_name().trim().replaceAll("\\s+", "_")
                    + "_" + contractDTO.getInvestor_name().trim().replaceAll("\\s+", "_") + ".pdf";

            s3Service.upload(s3Key, fileContent, "application/pdf");
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

    }


}
