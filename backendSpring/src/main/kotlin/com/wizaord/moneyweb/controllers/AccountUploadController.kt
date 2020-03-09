package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.services.AccountUploadResult
import com.wizaord.moneyweb.services.UploadFileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/moneyapi/family/{familyName}/accounts/{accountName}/upload")
@Scope("prototype")
class AccountUploadController(
        @Autowired private val uploadFileService: UploadFileService
) {

    @PostMapping("")
    @ResponseBody
    fun uploadFile(@PathVariable familyName: String,
                   @PathVariable accountName: String,
                   @RequestParam("file") file: MultipartFile): AccountUploadResult {
        return uploadFileService.loadFileForAccount(familyName, accountName, file.originalFilename!!, file.inputStream)
    }
}

