package servlet;
import java.io.File;
import java.io.FilenameFilter;
//Here we define class imageNameFiletr, which implements
//FilenameFilter interface.
class ImageNameFiletr implements FilenameFilter {
    String[] acceptedFileSuffixes = null;
    // Here we define the constructor so that it receives
    // an array of user-defined suffixes.
    public ImageNameFiletr(String[] suffixes) {
        acceptedFileSuffixes = suffixes;
    }
    @Override
    public boolean accept(File dir, String name) {
        String suffix = "";
        int dotIndex = -1;
        // Here we check whether there's a dot in the name of
        // the file. If there's, then we read the suffix
        // or part of the file name after the dot.
        if ((dotIndex = name.indexOf('.')) != -1)
            suffix = name.substring(dotIndex + 1);
        // Here we check whether the file suffix equals to
        // one of the accepted ones. If there's one equality,
        // the method returns true.
        for (String s : acceptedFileSuffixes) {
            if (s.equalsIgnoreCase(suffix))
                return true;
        }
        
        
        return false;
    }
}