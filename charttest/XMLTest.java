
package charttest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLTest {
    
    
    public static void main(String[] args) {
        try {
            // Setup a new eventReader
            InputStream in = new FileInputStream("sampleProject.xml");
            XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(in);
            
            while(eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                
                if(event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    
                    // Found a sizedistribution element
                    if(startElement.getName().getLocalPart().equals(XMLUtilities.SizeDistElements.ELEMENT)) {
                        SizeDistribution s = XMLUtilities.getSizeDist(eventReader);
                        int i = 0;
                    }
                }
                
                
            }
            
            eventReader.close();
            
        } catch (IOException | XMLStreamException ex) {
        }
    }
    
}
