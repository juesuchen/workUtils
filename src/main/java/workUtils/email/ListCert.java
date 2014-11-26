package workUtils.email;

/* 
 * Copyright 2006 Sun Microsystems, Inc.  All Rights Reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met: 
 * 
 *   - Redistributions of source code must retain the above copyright 
 *     notice, this list of conditions and the following disclaimer. 
 * 
 *   - Redistributions in binary form must reproduce the above copyright 
 *     notice, this list of conditions and the following disclaimer in the 
 *     documentation and/or other materials provided with the distribution. 
 * 
 *   - Neither the name of Sun Microsystems nor the names of its 
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS 
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Enumeration;

public class ListCert {

    /**
     * 
     * @Title: main
     * @Description: TODO(list the certifications in specified key-store)
     * @author juesu.chen
     * @date Nov 26, 2014 2:08:25 PM
     * @param @param args
     * @param @throws Exception 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void main(String[] args) throws Exception {

        String certFilePath = "D:/AM/AccessMatrix-5.3.0.3008-SIT/conf/testagent.keystore";
        certFilePath = "D:/AM/AccessMatrix-5.3.0.3008-SIT/tomcat/conf/testtrust.keystore";
        String password = "passphrase";
        File file = new File(certFilePath);
        System.out.println("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, password.toCharArray());
        in.close();

        for (Enumeration<String> e = ks.aliases(); e.hasMoreElements();) {
            System.out.println(e.nextElement());
        }
    }
}