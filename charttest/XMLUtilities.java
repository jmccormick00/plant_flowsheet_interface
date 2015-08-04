
package charttest;

import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;



public class XMLUtilities {
    public static class SizeDistElements {
        public static final String ELEMENT = "sizedist";
        public static final String NAME = "name";
        public static final String FITLINE = "fitline";
        public static final String VALUE = "value";
        public static final String SIZE = "size";
        public static final String RETAINED = "ret";
        private SizeDistElements() {}
    }
    
    static SizeDistribution getSizeDist(XMLEventReader reader) throws XMLStreamException {
        SizeDistribution s = new SizeDistribution();
        
        while(reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
                
                if(event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    
                    // Found a name element
                    if(startElement.getName().getLocalPart().equals(SizeDistElements.NAME)) {
                        event = reader.nextEvent();
                        s.changeName(event.asCharacters().getData());
                        continue;
                    }
                    // Found a fit element
                    if(startElement.getName().getLocalPart().equals(SizeDistElements.FITLINE)) {
                        event = reader.nextEvent();
                        String fit = event.asCharacters().getData();
                        if(fit.equals(LstSqFitLine.METHODAME)) {
                            s.setFitLine(new LstSqFitLine());
                        }
                        if(fit.equals(CubicSpline.METHODAME)) {
                            s.setFitLine(new CubicSpline());
                        }
                        continue;
                    }
                    // Found a value element
                    if(startElement.getName().getLocalPart().equals(SizeDistElements.VALUE)) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        float size = 0.0f, ret = 0.0f;
                        while(attributes.hasNext()) {
                            Attribute att = attributes.next();
                            if(att.getName().toString().equals(SizeDistElements.SIZE)) {
                                size = Float.parseFloat(att.getValue());
                            }
                            if(att.getName().toString().equals(SizeDistElements.RETAINED)) {
                                ret = Float.parseFloat(att.getValue());
                            }
                        }
                        s.setItems(size, ret, true);
                    }
                }
                if(event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if(endElement.getName().getLocalPart().equals(SizeDistElements.ELEMENT)) {
                        break;
                    }
                }
        }
        
        s.update();
        return s;
    }
    
//    static void writeSizeDist(SizeDistribution s, ) {
//    }
}
