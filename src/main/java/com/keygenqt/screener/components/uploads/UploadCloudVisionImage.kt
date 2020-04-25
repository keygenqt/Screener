/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.screener.components.uploads

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString
import com.keygenqt.screener.base.Configuration
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

class UploadCloudVisionImage {
    companion object {
        fun getString(filePath: String): String {

            val credStream = FileInputStream(File(Configuration.getCredentials()))
            val credentials = GoogleCredentials.fromStream(credStream)
            val imageAnnotatorSettings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

            ImageAnnotatorClient.create(imageAnnotatorSettings).use { vision ->

                val path = Paths.get(filePath)
                val data = Files.readAllBytes(path)
                val imgBytes = ByteString.copyFrom(data)
                val img = Image.newBuilder().setContent(imgBytes).build()
                val requests = arrayListOf<AnnotateImageRequest>()
                val feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()

                requests.add(AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build())

                val response = vision.batchAnnotateImages(requests)

                for (res in response.responsesList) {
                    if (res.hasError()) {
                        throw RuntimeException(res.error.message)
                    }
                    for (annotation in res.textAnnotationsList) {
                        return annotation.description
                    }
                }
            }
            return ""
        }
    }
}