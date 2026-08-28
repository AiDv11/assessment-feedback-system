package services;

import interfaces.Editor;
import java.util.ArrayList;
import java.util.List;
import utils.FileManager;


public class GenericFileEditor implements Editor {

    private final String filepath;
    private List<String> buffer = new ArrayList<>();

    public GenericFileEditor(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void loadData() {
        buffer = FileManager.readFile(filepath);
    }

    @Override
    public void saveChanges() {
        FileManager.writeFile(filepath, buffer);
    }

    public List<String> getBuffer() {
        return buffer;
    }
}
