/**
 * OWASP Benchmark Project v1.2
 *
 * <p>This file is part of the Open Web Application Security Project (OWASP) Benchmark Project. For
 * details, please see <a
 * href="https://owasp.org/www-project-benchmark/">https://owasp.org/www-project-benchmark/</a>.
 *
 * <p>The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, version 2.
 *
 * <p>The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * @author Dave Wichers
 * @created 2015
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/pathtraver-01/Benchmark01112")
public class Benchmark01112 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String param = "";
        java.util.Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();

            if (org.owasp.benchmark.helpers.Utils.commonHeaders.contains(name)) {
                continue; // If standard header, move on to next one
            }

            java.util.Enumeration<String> values = request.getHeaders(name);
            if (values != null && values.hasMoreElements()) {
                param = name; // Grabs the name of the first non-standard header as the parameter
                // value
                break;
            }
        }
        // Note: We don't URL decode header names because people don't normally do that

        String bar = new Test().doSomething(request, param);

        String fileName = null;
        java.io.FileInputStream fis = null;

        try {
            fileName = org.owasp.benchmark.helpers.Utils.TESTFILES_DIR + bar;
            fis = new java.io.FileInputStream(fileName);
            byte[] b = new byte[1000];
            int size = fis.read(b);
            response.getWriter()
                    .println(
                            "The beginning of file: '"
                                    + org.owasp.esapi.ESAPI.encoder().encodeForHTML(fileName)
                                    + "' is:\n\n");
            response.getWriter()
                    .println(org.owasp.esapi.ESAPI.encoder().encodeForHTML(new String(b, 0, size)));
        } catch (Exception e) {
            System.out.println("Couldn't open FileInputStream on file: '" + fileName + "'");
            //			System.out.println("File exception caught and swallowed: " + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (Exception e) {
                    // we tried...
                }
            }
        }
    } // end doPost

    private class Test {

        public String doSomething(HttpServletRequest request, String param)
                throws ServletException, IOException {

            String bar;
            String guess = "ABC";
            char switchTarget = guess.charAt(2);

            // Simple case statement that assigns param to bar on conditions 'A', 'C', or 'D'
            switch (switchTarget) {
                case 'A':
                    bar = param;
                    break;
                case 'B':
                    bar = "bobs_your_uncle";
                    break;
                case 'C':
                case 'D':
                    bar = param;
                    break;
                default:
                    bar = "bobs_your_uncle";
                    break;
            }

            return bar;
        }
    } // end innerclass Test
} // end DataflowThruInnerClass
