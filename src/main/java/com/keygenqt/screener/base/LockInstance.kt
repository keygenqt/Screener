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

package com.keygenqt.screener.base

import java.io.File
import java.io.RandomAccessFile

class LockInstance {
    companion object {
        fun lockInstance(lockFile: String): Boolean {
            try {
                val file = File(lockFile)
                val randomAccessFile = RandomAccessFile(file, "rw")
                val fileLock = randomAccessFile.channel.tryLock()
                if (fileLock != null) {
                    Runtime.getRuntime().addShutdownHook(Thread(Runnable {
                        try {
                            fileLock.release()
                            randomAccessFile.close()
                            file.delete()
                        } catch (e: Exception) {
                            println(e.message)
                            println("Unable to remove lock file: $lockFile")
                        }
                    }))
                    return true
                }
            } catch (e: Exception) {
                println(e.message)
                println("Unable to create and/or lock file: $lockFile")
            }
            return false
        }
    }
}