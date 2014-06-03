package ltu.d0007n;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

/**
 * This is a media file, i.e. a type defining both a name and the data
 * going by that name.
 */
@XmlType
public class MediaFile {
    public String fileName;
    public DataHandler fileData;
}
