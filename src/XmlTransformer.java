import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class XmlTransformer {

    public void transformXmlToHtml(String xmlFilePath, String xsltFilePath, String outputHtmlFilePath)
            throws TransformerException {
                
        Source xmlSource = new StreamSource(new File(xmlFilePath));
        Source xsltSource = new StreamSource(xsltFilePath);
        StreamResult result = new StreamResult(outputHtmlFilePath);

        TransformerFactory factory = TransformerFactory.newInstance();
        Templates templates = factory.newTemplates(xsltSource);
        Transformer transformer = templates.newTransformer();

        transformer.transform(xmlSource, result);

    }

    public void transformXmlToPdf(String xmlFilePath, String xslFoFilePath, String outputPdfFilePath)
            throws FOPException, TransformerException, IOException {
                
        Source xmlSource = new StreamSource(xmlFilePath);
        Source xslFoSource = new StreamSource(xslFoFilePath);

        // FOP Configuration
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // FOP instance for PDF
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xslFoSource);

        Result res = new SAXResult(fop.getDefaultHandler());
        transformer.transform(xmlSource, res);

        // Write to file
        try (OutputStream outputStream = new FileOutputStream(outputPdfFilePath)) {
            outputStream.write(out.toByteArray());
        }
    }

    public static void main(String[] args) {
        XmlTransformer transformer = new XmlTransformer();
        try {
            String xmlFilePath = "lib/input.xml";
            String xsltFilePath = "lib/stylesheet.xslt";
            String outputHtmlFilePath = "lib/output.html";
            String xslFoFilePath = "lib/stylesheet.xslfo";
            String outputPdfFilePath = "lib/output.pdf";

            transformer.transformXmlToHtml(xmlFilePath, xsltFilePath, outputHtmlFilePath);
            System.out.println("HTML transformation completed successfully!");

            transformer.transformXmlToPdf(xmlFilePath, xslFoFilePath, outputPdfFilePath);
            System.out.println("PDF transformation completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}