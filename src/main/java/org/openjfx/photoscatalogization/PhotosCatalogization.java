package org.openjfx.photoscatalogization;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;

import com.drew.metadata.Metadata;
import com.drew.metadata.xmp.XmpDirectory;

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPIterator;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.properties.XMPPropertyInfo;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * JavaFX App
 */
public class PhotosCatalogization extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Photos Catalogization");

        // Layouts
        VBox vbox = new VBox();

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        HBox hbox5 = new HBox();
        HBox hbox6 = new HBox();

        VBox vbox11 = new VBox();
        VBox vbox12 = new VBox();
        
        VBox vbox21 = new VBox();
        VBox vbox22 = new VBox();
        
        VBox vbox31 = new VBox();
        VBox vbox32 = new VBox();
        VBox vbox33 = new VBox();

        // Controls
        // Buttons
        Button addDirBtn = new Button("Add Dir");
        Button filterFilesByKeywBtn = new Button("Filter by keywords");

//        Labels
        Label chChoosedInputLvlLbl = new Label("Choosed directory input level:");
        Label chDefaultInputLvlLbl = new Label("Default directory input level:");

//        Checkboxes
        CheckBox maxChoosedInputLvlChB = new CheckBox("max. level");
        maxChoosedInputLvlChB.setSelected(true);

        CheckBox maxDefaultInputLvlChB = new CheckBox("max. level");
        maxDefaultInputLvlChB.setSelected(true);

//        Spinners
        final Spinner<Integer> chChoosedInputLvlSpnr = new Spinner<Integer>();
        final int choosedInitValue = 0;
        SpinnerValueFactory<Integer> choosedValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, choosedInitValue);
        chChoosedInputLvlSpnr.setValueFactory(choosedValueFactory);
        chChoosedInputLvlSpnr.setDisable(true);

        final Spinner<Integer> chDefaultInputLvlSpnr = new Spinner<Integer>();
        final int defaultInitValue = 0;
        SpinnerValueFactory<Integer> defaultValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, defaultInitValue);
        chDefaultInputLvlSpnr.setValueFactory(defaultValueFactory);
        chDefaultInputLvlSpnr.setDisable(true);

//        Checkboxes events
        EventHandler<ActionEvent> choosedChBEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (maxChoosedInputLvlChB.isSelected()) {
                    chChoosedInputLvlSpnr.setDisable(true);
                } else {
                    chChoosedInputLvlSpnr.setDisable(false);
                }
            }
        };
        maxChoosedInputLvlChB.setOnAction(choosedChBEvent);

        EventHandler<ActionEvent> defaultChBEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (maxDefaultInputLvlChB.isSelected()) {
                    chDefaultInputLvlSpnr.setDisable(true);
                } else {
                    chDefaultInputLvlSpnr.setDisable(false);
                }
            }
        };
        maxDefaultInputLvlChB.setOnAction(defaultChBEvent);

        // Tables
        // Directories with files Table
        TableView<PhotosDir> tableDir = addTableDir();

        // Files in directory Table
        TableView<PhotosDirFile> tableDirFiles = addTableDirFiles();

        // File's keywords Table
        TableView<FileKeyword> tableFileKeywords = addTableFileKeywords();

        // Found files Table
        TableView<FoundFile> tableFoundFiles = addTableFoundFiles();

        // Found files' keywords Table
        TableView<FoundFilesKeyword> tableFoundFilesKeywords = addTableFoundFilesKeywords();

        // Found files' operations Table
        TableView<FoundFilesOperation> tableFoundFilesOperations = addTableFoundFilesOperations();
//        /Tables
        
//        TreeViews
        TreeItem<String> rootItem = new TreeItem<String>("<Root of Tree>");
        rootItem.setExpanded(true);
        TreeView<String> dirsFilesKeywordsTree = new TreeView<String>(rootItem);
        
        // Search Rule Text Area
        TextArea searchRule = new TextArea();
        searchRule.setWrapText(true);
        searchRule.setPromptText("Enter keywords with AND, OR, NO operands there...");
        
        List<String> imagesFilesExtensions = List.of(".jpg", ".jpeg", ".jpe",
                ".tif", ".tiff", ".png");
        
        ArrayList<TreeNode<DirectoryInfo>> dirTreesList = new ArrayList<TreeNode<DirectoryInfo>>();

        Set<String> allKeywordsSet = new HashSet<String>();

        // Add Directory Button Event
        addDirBtn.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                DirectoryChooser addDirChooser = new DirectoryChooser();
                File addDirSelected = addDirChooser.showDialog(primaryStage);
                if (addDirSelected != null) {
                    String addDirPath = addDirSelected.getAbsolutePath();
                    System.out.println(addDirPath);
                    PhotosDir addedDir = new PhotosDir(2L, addDirPath, "No inserted directories", 0);
                    tableDir.getItems().add(addedDir);

                    if (addDirSelected.isDirectory()) {
                        int level;
                        if (chDefaultInputLvlSpnr.isDisable()) {
                            level = -1;
                        } else {
                            level = chDefaultInputLvlSpnr.getValue();
                        }
                        System.out.println("level is " + Integer.toString(level));
                        TreeNode<DirectoryInfo> dirTree = buildDirectoryTree(addDirSelected, imagesFilesExtensions, allKeywordsSet, level);
                        dirTreesList.add(dirTree);
                        printDirectoryTree(dirTree);
//                        20.06.2020
//                        This method adds directories and files to TreeView
                        passChildrenDirTree(rootItem, dirTree);
//                        /20.06.2020
                        getFilesFromDirTree(dirTree, imagesFilesExtensions, allKeywordsSet, tableDirFiles, tableFileKeywords);
                    }
                }
//                print files paths and their keywords from all added directories
                System.out.println("=== *** Information about all files *** ===");
                for (TreeNode<DirectoryInfo> treeNode : dirTreesList) {
                    for (TreeNode<DirectoryInfo> node : treeNode) {
                        DirectoryInfo dirInfo = node.data;
                        String dirName = dirInfo.getDirectoryPath();
                        ArrayList<FileInfo> dirFiles = dirInfo.getFilesList();
//                        print directory name
                        System.out.println("*** Directory name *** : " + dirName);
//                        Add files paths from directories tree to tableDirFiles
                        for (FileInfo fInfo : dirFiles) {
                            for (String s : imagesFilesExtensions) {
                                if (fInfo.getFilePath().toLowerCase().endsWith(s)) {
//                                    print file name and file's keywords
                                    System.out.println("*** File path *** : " + fInfo.getFilePath());
                                    Set<String> keywordsSet = fInfo.getFileKeywordsSet();
                                    System.out.println("*** File keywords *** : ");
                                    for (String keyword : keywordsSet) {
                                        System.out.println(keyword);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        );

//        Actions to build filter conditions binary tree by text from text area
        filterFilesByKeywBtn.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                String filterConditionText = searchRule.getText();
                
                System.out.println("*** Start buildFilterConditionTree ***");
                TreeNode<FilterConditionElement> filterTree = buildFilterConditionTree(filterConditionText);
                System.out.println("*** Start printFilterConditionTree ***");
                printFilterConditionTree(filterTree);

                System.out.println("*** Filter condition text *** :");
                System.out.println(filterConditionText);
                
                System.out.println("*** Directories' trees before filtration ***");
                for (TreeNode<DirectoryInfo> dirTree : dirTreesList) {
                    printDirectoryTree(dirTree);
                }
                
                System.out.println("*** Start filterFilesByCondition ***");
                ArrayList<TreeNode<DirectoryInfo>> dirTreesFilteredList = 
                        filterFilesByCondition(dirTreesList, filterTree);
                
                System.out.println("*** Directories' trees after filtration ***");
                for (TreeNode<DirectoryInfo> dirTree : dirTreesFilteredList) {
                    printDirectoryTree(dirTree);
                }
            }
        }
        );

        // Add all controls to layouts
        vbox11.getChildren().addAll(chChoosedInputLvlLbl, maxChoosedInputLvlChB, chChoosedInputLvlSpnr,
                chDefaultInputLvlLbl, maxDefaultInputLvlChB, chDefaultInputLvlSpnr);

        hbox1.getChildren().addAll(addDirBtn, filterFilesByKeywBtn);
        hbox2.getChildren().addAll(tableDir, vbox11, tableDirFiles, vbox12, dirsFilesKeywordsTree);
        hbox4.getChildren().addAll(tableFileKeywords, vbox21, searchRule, vbox22);
        hbox6.getChildren().addAll(tableFoundFiles, vbox31, tableFoundFilesKeywords, vbox32, tableFoundFilesOperations, vbox33);

        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6);

        Scene scene = new Scene(vbox, 640, 480);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private ObservableList<KeywordFromList> getNoAllKeywordsList() {
        KeywordFromList noAllKeywords = new KeywordFromList(1L, 0, "No all keywords list");

        ObservableList<KeywordFromList> list = FXCollections.observableArrayList(noAllKeywords);
        return list;
    }

    private ObservableList<FoundFile> getNoFoundFilesList() {
        FoundFile noFoundFiles = new FoundFile(1L, "No found files");

        ObservableList<FoundFile> list = FXCollections.observableArrayList(noFoundFiles);
        return list;
    }

    private ObservableList<FoundFilesKeyword> getNoFoundFilesKeywordsList() {
        FoundFilesKeyword noFoundFilesKeywords = new FoundFilesKeyword(1L, "No found file's keywords");

        ObservableList<FoundFilesKeyword> list = FXCollections.observableArrayList(noFoundFilesKeywords);
        return list;
    }

    private ObservableList<FoundFilesOperation> getNoFoundFilesOperationsList() {
        FoundFilesOperation noFoundFilesOperations = new FoundFilesOperation(1L, "No found files' operations");

        ObservableList<FoundFilesOperation> list = FXCollections.observableArrayList(noFoundFilesOperations);
        return list;
    }

    private TableView<PhotosDir> addTableDir() {
        TableView<PhotosDir> tableDir = new TableView<PhotosDir>();

        TableColumn<PhotosDir, String> photosDirPathCol = new TableColumn<PhotosDir, String>("Directory Path");

        TableColumn<PhotosDir, String> photosDirInsDirsCol = new TableColumn<PhotosDir, String>("Inserted Directories");

        TableColumn<PhotosDir, Integer> photosDirInsLevelCol = new TableColumn<PhotosDir, Integer>("Level");

        photosDirPathCol.setCellValueFactory(new PropertyValueFactory<>("photosDirPath"));
        photosDirInsDirsCol.setCellValueFactory(new PropertyValueFactory<>("photosDirInsDirs"));
        photosDirInsLevelCol.setCellValueFactory(new PropertyValueFactory<>("photosDirInsLevel"));
        
        tableDir.getColumns().addAll(photosDirPathCol, photosDirInsDirsCol, photosDirInsLevelCol);

        return tableDir;
    }

    private TableView<PhotosDirFile> addTableDirFiles() {
        TableView<PhotosDirFile> tableDirFiles = new TableView<PhotosDirFile>();

        TableColumn<PhotosDirFile, String> photosDirFileNameCol = new TableColumn<PhotosDirFile, String>("File Name");

        photosDirFileNameCol.setCellValueFactory(new PropertyValueFactory<>("photosDirFileName"));
        
        tableDirFiles.getColumns().addAll(photosDirFileNameCol);

        return tableDirFiles;
    }

    private TableView<FileKeyword> addTableFileKeywords() {
        TableView<FileKeyword> tableFileKeywords = new TableView<FileKeyword>();

        TableColumn<FileKeyword, String> fileKeywordsCol = new TableColumn<FileKeyword, String>("Keyword");

        fileKeywordsCol.setCellValueFactory(new PropertyValueFactory<>("fileKeyword"));
        
        tableFileKeywords.getColumns().addAll(fileKeywordsCol);

        return tableFileKeywords;
    }

    private TableView<KeywordFromList> addTableAllKeywordsList() {
        TableView<KeywordFromList> tableAllKeywordsList = new TableView<KeywordFromList>();

        TableColumn<KeywordFromList, Integer> keywordFromListCountCol = new TableColumn<KeywordFromList, Integer>("Count");

        TableColumn<KeywordFromList, String> keywordFromListCol = new TableColumn<KeywordFromList, String>("Keyword");

        keywordFromListCountCol.setCellValueFactory(new PropertyValueFactory<>("keywordFromListCount"));
        keywordFromListCol.setCellValueFactory(new PropertyValueFactory<>("keywordFromList"));

        ObservableList<KeywordFromList> allKeywordsList = getNoAllKeywordsList();
        tableAllKeywordsList.setItems(allKeywordsList);

        tableAllKeywordsList.getColumns().addAll(keywordFromListCountCol, keywordFromListCol);

        return tableAllKeywordsList;
    }

    private TableView<FoundFile> addTableFoundFiles() {
        TableView<FoundFile> tableFoundFiles = new TableView<FoundFile>();

        TableColumn<FoundFile, String> foundFileNameCol = new TableColumn<FoundFile, String>("Found File");

        foundFileNameCol.setCellValueFactory(new PropertyValueFactory<>("foundFileName"));

        ObservableList<FoundFile> foundFiles = getNoFoundFilesList();
        tableFoundFiles.setItems(foundFiles);

        tableFoundFiles.getColumns().addAll(foundFileNameCol);

        return tableFoundFiles;
    }

    private TableView<FoundFilesKeyword> addTableFoundFilesKeywords() {
        TableView<FoundFilesKeyword> tableFoundFilesKeywords = new TableView<FoundFilesKeyword>();

        TableColumn<FoundFilesKeyword, String> foundFilesKeywordCol = new TableColumn<FoundFilesKeyword, String>("Found File's Keyword");

        foundFilesKeywordCol.setCellValueFactory(new PropertyValueFactory<>("foundFilesKeyword"));

        ObservableList<FoundFilesKeyword> foundFilesKeywords = getNoFoundFilesKeywordsList();
        tableFoundFilesKeywords.setItems(foundFilesKeywords);

        tableFoundFilesKeywords.getColumns().addAll(foundFilesKeywordCol);

        return tableFoundFilesKeywords;
    }

    private TableView<FoundFilesOperation> addTableFoundFilesOperations() {
        TableView<FoundFilesOperation> tableFoundFilesOperations = new TableView<FoundFilesOperation>();

        TableColumn<FoundFilesOperation, String> foundFilesOperationCol = new TableColumn<FoundFilesOperation, String>("Found files' operation");

        foundFilesOperationCol.setCellValueFactory(new PropertyValueFactory<>("foundFilesOperation"));

        ObservableList<FoundFilesOperation> foundFilesOperations = getNoFoundFilesOperationsList();
        tableFoundFilesOperations.setItems(foundFilesOperations);

        tableFoundFilesOperations.getColumns().addAll(foundFilesOperationCol);

        return tableFoundFilesOperations;
    }

//    20.06.2020
    private void passChildrenDirTree(TreeItem<String> rootItem, TreeNode<DirectoryInfo> dirTree) {
        TreeItem<String> dirItem = new TreeItem<String>(dirTree.data.getDirectoryPath());
        dirItem.setExpanded(true);
        rootItem.getChildren().add(dirItem);
        if (dirTree.children.size() > 0) {
            for (TreeNode<DirectoryInfo> childDirTree : dirTree.children) {
                passChildrenDirTree(dirItem, childDirTree);
            }
        }
        for (FileInfo fInfo : dirTree.data.getFilesList()) {
            TreeItem<String> fileItem = new TreeItem<String>(fInfo.getFilePath());
            fileItem.setExpanded(true);
            dirItem.getChildren().add(fileItem);
            
            for (String keywordStr : fInfo.getFileKeywordsSet()) {
                TreeItem<String> keywordItem = new TreeItem<String>(keywordStr);
                fileItem.getChildren().add(keywordItem);
            }
        }
    }
//    /20.06.2020
    
    //    Methods to build directory tree
    public static TreeNode<DirectoryInfo> buildDirectoryTree(File folder, List<String> imagesFilesExtensions, Set<String> allKeywordsSet, int level) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        int indent = 0; // input directories deapth
        TreeNode<DirectoryInfo> node = buildDirectoryTree(folder, imagesFilesExtensions, allKeywordsSet, indent, level);
        return node;
    }

    private static TreeNode<DirectoryInfo> buildDirectoryTree(File folder, List<String> imagesFilesExtensions, Set<String> allKeywordsSet, int indent, int level) {
//        get directory info
        DirectoryInfo dirInfoRoot = getDirectoryInfo(folder, imagesFilesExtensions, allKeywordsSet);
        TreeNode<DirectoryInfo> root = new TreeNode<DirectoryInfo>(dirInfoRoot);
        if (level != 0) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    buildDirectoryTree(file, imagesFilesExtensions, allKeywordsSet, indent + 1, level, root);
                }
            }
        }
        return root;
    }

    private static void buildDirectoryTree(File folder, List<String> imagesFilesExtensions, Set<String> allKeywordsSet, int indent, int level, TreeNode<DirectoryInfo> node) {
//        get directory info
        DirectoryInfo dirInfoNodeNew = getDirectoryInfo(folder, imagesFilesExtensions, allKeywordsSet);
        TreeNode<DirectoryInfo> nodeNew = node.addChild(dirInfoNodeNew);
        if (indent < level || level == -1) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    buildDirectoryTree(file, imagesFilesExtensions, allKeywordsSet, indent + 1, level, nodeNew);
                }
            }
        }
    }

//    Create indent to print tree to console output
    private static String createIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static DirectoryInfo getDirectoryInfo(File folder, List<String> imagesFilesExtensions, Set<String> allKeywordsSet) {
        ArrayList<FileInfo> fileInfoNode = new ArrayList<FileInfo>();
        for (File f : folder.listFiles()) {
            if (!f.isDirectory()) {
                String fileName = new String(f.getAbsolutePath());
                Metadata metadata = new Metadata();
                Set<String> fKeywSet = new HashSet();
//                !!! to add xmp metadata
                for (String s : imagesFilesExtensions) {
                    if (fileName.toLowerCase().endsWith(s)) {
                        if (f.exists()) {
                            try {
                                metadata = ImageMetadataReader.readMetadata(f);
                                
                                for (XmpDirectory xmpDirectory : metadata.getDirectoriesOfType(XmpDirectory.class)) {
                                    XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
                                    XMPIterator itr = xmpMeta.iterator();

                                    while (itr.hasNext()) {
                                        XMPPropertyInfo property = (XMPPropertyInfo) itr.next();
                                        
                                        if (property.getPath() != null) {
                                            if (property.getPath().toLowerCase().contains("subject")
                                                    && property.getValue().length() > 0) {
                                                System.out.println(property.getValue());
                                                fKeywSet.add(property.getValue());
                                                allKeywordsSet.add(property.getValue());
                                            }
                                        }
                                    }
                                }
                            } catch (ImageProcessingException ex) {
                                ex.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (XMPException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                
                FileInfo fInfo = new FileInfo(f.getPath(), metadata, fKeywSet);
                fileInfoNode.add(fInfo);
            }
        }
        DirectoryInfo dirInfoNode = new DirectoryInfo(folder.getPath(), fileInfoNode);
        return dirInfoNode;
    }

    public static void printDirectoryTree(TreeNode<DirectoryInfo> dirTree) {
        for (TreeNode<DirectoryInfo> node : dirTree) {
            String indent = createIndent(node.getLevel());
            DirectoryInfo dirInfo = node.data;
            String dirName = dirInfo.getDirectoryPath();
            ArrayList<FileInfo> dirFiles = dirInfo.getFilesList();
            System.out.println(indent + dirName);

            printFilesListFromDir(dirFiles, indent, node.getLevel());
        }
    }

    private static void printFilesListFromDir(ArrayList<FileInfo> dirFiles, String indent, int level) {
        for (FileInfo fInfo : dirFiles) {
            System.out.println(indent + "* " + fInfo.getFilePath());
//            Print file's keywords
            for (String keyword : fInfo.getFileKeywordsSet()) {
                System.out.println(indent + "** " + keyword);
            }
        }
    }
    
    public void getFilesFromDirTree(TreeNode<DirectoryInfo> dirTree, List<String> imagesFilesExtensions,
            Set<String> allKeywordsSet, TableView<PhotosDirFile> tableDirFiles, TableView<FileKeyword> tableFileKeywords) {
        for (TreeNode<DirectoryInfo> node : dirTree) {
            DirectoryInfo dirInfo = node.data;
            String dirName = dirInfo.getDirectoryPath();
            ArrayList<FileInfo> dirFiles = dirInfo.getFilesList();

//            Add files paths from directories tree to tableDirFiles
            for (FileInfo fInfo : dirFiles) {
                for (String s : imagesFilesExtensions) {
                    if (fInfo.getFilePath().toLowerCase().endsWith(s)) {
                        PhotosDirFile fileRow = new PhotosDirFile(1L, fInfo.getFilePath());
                        tableDirFiles.getItems().add(fileRow);
                    }
                }
            }
        }
                
//        Add keywords from 'all keywords set' to tableFileKeywords
        for (String keywFromAllKeywSet : allKeywordsSet) {
            FileKeyword fileKeywRow = new FileKeyword(1L, keywFromAllKeywSet);
            tableFileKeywords.getItems().add(fileKeywRow);
        }
    }
    
    public static TreeNode<FilterConditionElement> buildFilterConditionTree(String filterConditionText) {
        ArrayList<FilterConditionElement> originalConditionArray = new ArrayList<FilterConditionElement>();

        char[] conditionCharArray = filterConditionText.toCharArray();
        String operatorStr = "";
        String keywordStr = "";
        boolean isOperatorBrackets = false;
        for (int i = 0; i < conditionCharArray.length; i++) {
            if (conditionCharArray[i] == '[') {
                FilterConditionElement opSqBrEl = new FilterConditionElement("[", false, false, true);
                originalConditionArray.add(opSqBrEl);
            } else if (conditionCharArray[i] == ']') {
                if (!keywordStr.isEmpty()) {
                    FilterConditionElement keywordEl = new FilterConditionElement(keywordStr, false, false, false);
                    keywordStr = "";
                    originalConditionArray.add(keywordEl);
                }
                FilterConditionElement clSqBrEl = new FilterConditionElement("]", false, false, true);
                originalConditionArray.add(clSqBrEl);
            } else if (conditionCharArray[i] == '<') {
                if (!keywordStr.isEmpty()) {
                    FilterConditionElement keywordEl = new FilterConditionElement(keywordStr, false, false, false);
                    keywordStr = "";
                    originalConditionArray.add(keywordEl);
                }
                isOperatorBrackets = true;
            } else if (isOperatorBrackets) {
                if (conditionCharArray[i] == '>') {
                    isOperatorBrackets = false;
                    FilterConditionElement operatorEl = new FilterConditionElement(operatorStr, false, true, false);
                    operatorStr = "";
                    originalConditionArray.add(operatorEl);
                } else {
                    operatorStr = operatorStr + conditionCharArray[i];
                }
            } else {
                keywordStr = keywordStr + conditionCharArray[i];
                if (i == conditionCharArray.length - 1) {
                    FilterConditionElement keywordEl = new FilterConditionElement(keywordStr, false, false, false);
                    keywordStr = "";
                    originalConditionArray.add(keywordEl);
                }
            }
        }
        
        System.out.println("*** String splitted to elements: ***");
        for (FilterConditionElement fCElement : originalConditionArray) {
            System.out.println(fCElement.getFilterConditionStr());
            System.out.println("<" + fCElement.getIsOperator() + ">");
            System.out.println("[] " + fCElement.getIsSqBracket());
            System.out.println("no " + fCElement.getNoProperty());
            System.out.println("***");
        }
        System.out.println("*** /String splitted to elements: ***");
        
        TreeNode<FilterConditionElement> node = null;
        System.out.println("*** Start buildFilterConditionTreePrivate ***");
        TreeNode<FilterConditionElement> nodeReturned = buildFilterConditionTreePrivate(originalConditionArray, node, true);

        return nodeReturned;
    }
    
    private static TreeNode<FilterConditionElement> buildFilterConditionTreePrivate(ArrayList<FilterConditionElement> conditionArray,
            TreeNode<FilterConditionElement> root, boolean toMakeRoot) {
//        Define [] brackets position and write it to ArrayList<Integer>
        int openingSqBrCount = 0;
        int closingSqBrCount = 0;
        ArrayList<Integer> openingSqBrNums = new ArrayList<Integer>();
        ArrayList<Integer> closingSqBrNums = new ArrayList<Integer>();
        defineBrackets(conditionArray, openingSqBrCount, closingSqBrCount, openingSqBrNums, closingSqBrNums);

        System.out.println("***");
        System.out.println("toMakeRoot = " + toMakeRoot);
        System.out.println("openingSqBrNums size = " + openingSqBrNums.size());
        for (int i = 0; i < openingSqBrNums.size(); i++) {
            System.out.println("[ = " + openingSqBrNums.get(i) + ", ] = " + closingSqBrNums.get(i));
        }
        System.out.println("* Condition elements *");
        for (FilterConditionElement fCElement : conditionArray) {
            System.out.println(fCElement.getFilterConditionStr());
        }
        System.out.println("* /Condition elements *");
        
        boolean isNoProperty = false;

        FilterConditionElement orOperator = new FilterConditionElement("or", false, true, false);
        FilterConditionElement andOperator = new FilterConditionElement("and", false, true, false);

//        Remove [] brackets
        removeSqBrackets(conditionArray, isNoProperty,
                openingSqBrCount, closingSqBrCount,
                openingSqBrNums, closingSqBrNums);

        System.out.println("After removing brackets");
        System.out.println("openingSqBrNums size = " + openingSqBrNums.size());
        for (int i = 0; i < openingSqBrNums.size(); i++) {
            System.out.println("[ = " + openingSqBrNums.get(i) + ", ] = " + closingSqBrNums.get(i));
        }
        System.out.println("* Condition elements *");
        for (FilterConditionElement fCElement : conditionArray) {
            System.out.println(fCElement.getFilterConditionStr());
        }
        System.out.println("* /Condition elements *");
        
//        if (ArrayList<FilterConditionElement> contains "or" or "and" 
//        binary operator NOT in defined [] brackets)
//        Make a list and a set of numbers of condition's elements
        List<Integer> conditionArrayNumsRange = IntStream.rangeClosed(0, conditionArray.size() - 1).boxed().collect(Collectors.toList());
        Set<Integer> conditionArrayNumsSet = new HashSet<Integer>(conditionArrayNumsRange);

//        Make set only with numbers of elements which AREN'T positioned in defined squared brackets
        for (int i = 0; i < openingSqBrNums.size(); i++) {
            List<Integer> inSqBracketsNumsRange = IntStream.rangeClosed(openingSqBrNums.get(i), closingSqBrNums.get(i)).boxed().collect(Collectors.toList());
            Set<Integer> inSqBracketsNumsSet = new HashSet<Integer>(inSqBracketsNumsRange);
            conditionArrayNumsSet.removeAll(inSqBracketsNumsSet);
        }
        
        System.out.println("* Set of numbers which aren't positioned in def. sq. br. *");
        System.out.println(conditionArrayNumsSet);
        System.out.println("* /Set of numbers which aren't positioned in def. sq. br. *");
        
//        Make ArrayList<FilterConditionElement> of condition elements which AREN'T positioned in defined squared brackets
        ArrayList<FilterConditionElement> conditionElsNotInSqBr = new ArrayList<FilterConditionElement>();
        for (Integer elNum : new ArrayList<Integer>(conditionArrayNumsSet)) {
            conditionElsNotInSqBr.add(conditionArray.get(elNum));
        }

        System.out.println("* Array of elements which aren't positioned in def. sq. br. *");
        for (FilterConditionElement fCElement : conditionElsNotInSqBr) {
            System.out.println(fCElement.getFilterConditionStr());
        }
        System.out.println("* /Array of elements which aren't positioned in def. sq. br. *");
        
//        Walk to elements from conditionArrayNumsSet only
        BooleanOrAnd isOrOperator = new BooleanOrAnd(false);
        BooleanOrAnd isAndOperator = new BooleanOrAnd(false);
        isOrAnd(conditionElsNotInSqBr, isOrOperator, isAndOperator);
        System.out.println("* isOrAnd *");
        System.out.println(isOrOperator.getBooleanOrAnd());
        System.out.println(isAndOperator.getBooleanOrAnd());
        System.out.println("* /isOrAnd *");
        
        if (isOrOperator.getBooleanOrAnd()) {
            System.out.println("* conditionElsNotInSqBr contains or *");
            for (Integer elNum : new ArrayList<Integer>(conditionArrayNumsSet)) {
                if (conditionArray.get(elNum).getIsOperator()
                        && conditionArray.get(elNum).getFilterConditionStr().equals("or")) {
                    System.out.println("* or num = " + elNum + " *");
                    ArrayList<FilterConditionElement> subArrayBeforeOr
                            = new ArrayList<FilterConditionElement>(conditionArray.subList(0, elNum));
                    ArrayList<FilterConditionElement> subArrayAfterOr
                            = new ArrayList<FilterConditionElement>(conditionArray.subList(elNum + 1, conditionArray.size()));
                    System.out.println("* Array of elements before or *");
                    for (FilterConditionElement fCElement : subArrayBeforeOr) {
                        System.out.println(fCElement.getFilterConditionStr());
                    }
                    System.out.println("* /Array of elements before or *");
                    System.out.println("* Array of elements after or *");
                    for (FilterConditionElement fCElement : subArrayAfterOr) {
                        System.out.println(fCElement.getFilterConditionStr());
                    }
                    System.out.println("* /Array of elements after or *");
                    System.out.println("isNoProperty = " + isNoProperty);
                    orOperator.setNoProperty(isNoProperty);
                    System.out.println("toMakeRoot = " + toMakeRoot);
                    if (toMakeRoot) {
                        root = new TreeNode<FilterConditionElement>(orOperator);
                        buildFilterConditionTreePrivate(subArrayBeforeOr, root, false);
                        buildFilterConditionTreePrivate(subArrayAfterOr, root, false);
                    } else {
                        TreeNode<FilterConditionElement> node = root.addChild(orOperator);
                        buildFilterConditionTreePrivate(subArrayBeforeOr, node, false);
                        buildFilterConditionTreePrivate(subArrayAfterOr, node, false);
                    }
                    break;
                }
            }
        } else if (isAndOperator.getBooleanOrAnd()) {
            System.out.println("* conditionElsNotInSqBr contains and *");
            for (Integer elNum : new ArrayList<Integer>(conditionArrayNumsSet)) {
                if (conditionArray.get(elNum).getIsOperator()
                        && conditionArray.get(elNum).getFilterConditionStr().equals("and")) {
                    System.out.println("* and num = " + elNum + " *");
                    ArrayList<FilterConditionElement> subArrayBeforeAnd
                            = new ArrayList<FilterConditionElement>(conditionArray.subList(0, elNum));
                    ArrayList<FilterConditionElement> subArrayAfterAnd
                            = new ArrayList<FilterConditionElement>(conditionArray.subList(elNum + 1, conditionArray.size()));
                    System.out.println("* Array of elements before and *");
                    for (FilterConditionElement fCElement : subArrayBeforeAnd) {
                        System.out.println(fCElement.getFilterConditionStr());
                    }
                    System.out.println("* /Array of elements before and *");
                    System.out.println("* Array of elements after and *");
                    for (FilterConditionElement fCElement : subArrayAfterAnd) {
                        System.out.println(fCElement.getFilterConditionStr());
                    }
                    System.out.println("* /Array of elements after and *");
                    System.out.println("isNoProperty = " + isNoProperty);
                    andOperator.setNoProperty(isNoProperty);
                    System.out.println("toMakeRoot = " + toMakeRoot);
                    if (toMakeRoot) {
                        root = new TreeNode<FilterConditionElement>(andOperator);
                        buildFilterConditionTreePrivate(subArrayBeforeAnd, root, false);
                        buildFilterConditionTreePrivate(subArrayAfterAnd, root, false);
                    } else {
                        TreeNode<FilterConditionElement> node = root.addChild(andOperator);
                        buildFilterConditionTreePrivate(subArrayBeforeAnd, node, false);
                        buildFilterConditionTreePrivate(subArrayAfterAnd, node, false);
                    }
                    break;
                }
            }
        } else {
            if (conditionArray.size() == 2 && conditionArray.get(0).getIsOperator()
                    && conditionArray.get(0).getFilterConditionStr().equals("no")) {
                System.out.println("* conditionArray size = 2, first element = no *");
                FilterConditionElement keywordElement = conditionArray.get(conditionArray.size() - 1);
                System.out.println("* keywordElement = " + keywordElement.getFilterConditionStr() + " *");
                System.out.println("isNoProperty = " + isNoProperty);
                keywordElement.setNoProperty(!isNoProperty);
                System.out.println("toMakeRoot = " + toMakeRoot);
                if (toMakeRoot) {
                    root = new TreeNode<FilterConditionElement>(keywordElement);
                } else {
                    root.addChild(keywordElement);
                }
            } else {
                System.out.println("* else *");
                FilterConditionElement keywordElement = conditionArray.get(0);
                System.out.println("* keywordElement = " + keywordElement.getFilterConditionStr() + " *");
                System.out.println("isNoProperty = " + isNoProperty);
                keywordElement.setNoProperty(isNoProperty);
                System.out.println("toMakeRoot = " + toMakeRoot);
                if (toMakeRoot) {
                    root = new TreeNode<FilterConditionElement>(keywordElement);
                } else {
                    root.addChild(keywordElement);
                }
            }
        }
        
        return root;
    }
    
    private static void removeSqBrackets 
        (ArrayList<FilterConditionElement> conditionArray, boolean isNoProperty,
            int openingSqBrCount, int closingSqBrCount,
            ArrayList<Integer> openingSqBrNums, ArrayList<Integer> closingSqBrNums) {
        if ((openingSqBrNums.size() == 1 && openingSqBrNums.get(0) == 0
                && closingSqBrNums.get(0) == conditionArray.size() - 1)
                || (openingSqBrNums.size() == 1 && openingSqBrNums.get(0) == 1
                && closingSqBrNums.get(0) == conditionArray.size() - 1
                && conditionArray.get(0).getIsOperator() && conditionArray.get(0).getFilterConditionStr().equals("no"))) {
            while ((openingSqBrNums.size() == 1 && openingSqBrNums.get(0) == 0
                    && closingSqBrNums.get(0) == conditionArray.size() - 1)
                    || (openingSqBrNums.size() == 1 && openingSqBrNums.get(0) == 1
                    && closingSqBrNums.get(0) == conditionArray.size() - 1
                    && conditionArray.get(0).getIsOperator() && conditionArray.get(0).getFilterConditionStr().equals("no"))) {
                if (openingSqBrNums.size() == 1 && openingSqBrNums.get(0) == 0
                        && closingSqBrNums.get(0) == conditionArray.size() - 1) {
                    conditionArray.remove(0);
                    conditionArray.remove(conditionArray.size() - 1);
                } else if (openingSqBrNums.size() == 1 && openingSqBrNums.get(0) == 1
                        && closingSqBrNums.get(0) == conditionArray.size() - 1
                        && conditionArray.get(0).getIsOperator() && conditionArray.get(0).getFilterConditionStr().equals("no")) {
                    conditionArray.remove(1);
                    conditionArray.remove(conditionArray.size() - 1);
                    isNoProperty = !isNoProperty;
                    conditionArray.remove(0);
                }
                defineBrackets(conditionArray, openingSqBrCount, closingSqBrCount, openingSqBrNums, closingSqBrNums);
            }
        }
    }
    
    private static void isOrAnd(ArrayList<FilterConditionElement> conditionArray, 
            BooleanOrAnd isOrOperator, BooleanOrAnd isAndOperator) {
        for (FilterConditionElement fCElement : conditionArray) {
            if (fCElement.getIsOperator() && fCElement.getFilterConditionStr().equals("or")) {
                isOrOperator.setBooleanOrAnd(true);
                break;
            }
        }
        if (!isOrOperator.getBooleanOrAnd()) {
            for (FilterConditionElement fCElement : conditionArray) {
                if (fCElement.getIsOperator() && fCElement.getFilterConditionStr().equals("and")) {
                    isAndOperator.setBooleanOrAnd(true);
                    break;
                }
            }
        }
    }
    
//    06.12.2019
    public static void printFilterConditionTree(TreeNode<FilterConditionElement> filterTree) {
        for (TreeNode<FilterConditionElement> node : filterTree) {
            String indent = createIndent(node.getLevel());
            FilterConditionElement filterInfo = node.data;
            
            String filterStr = filterInfo.getFilterConditionStr();
            boolean noProperty = filterInfo.getNoProperty();
            boolean isOperator = filterInfo.getIsOperator();
            
            if (isOperator && noProperty) {
                System.out.println(indent + "<no>" + "<" + filterStr + ">");
            } else if (isOperator && !noProperty) {
                System.out.println(indent + "<" + filterStr + ">");
            } else if (!isOperator && noProperty) {
                System.out.println(indent + "<no>" + filterStr);
            } else if (!isOperator && !noProperty) {
                System.out.println(indent + filterStr);
            }
        }
    }
    
//    04.12.2019
//    Works correctly
    public static void defineBrackets(ArrayList<FilterConditionElement> conditionArray, 
            int openingSqBrCount, int closingSqBrCount, 
            ArrayList<Integer> openingSqBrNums, ArrayList<Integer> closingSqBrNums) {
        openingSqBrCount = 0;
        closingSqBrCount = 0;
        openingSqBrNums.clear();
        closingSqBrNums.clear();
        for (int i = 0; i < conditionArray.size(); i++) {
            if (openingSqBrCount == 0 && conditionArray.get(i).getIsSqBracket()
                    && conditionArray.get(i).getFilterConditionStr() == "[") {
                openingSqBrNums.add(i);
                openingSqBrCount++;
            } else if (openingSqBrCount > 0 && openingSqBrCount == closingSqBrCount + 1
                    && conditionArray.get(i).getIsSqBracket() && conditionArray.get(i).getFilterConditionStr() == "]") {
                closingSqBrNums.add(i);
                openingSqBrCount = 0;
                closingSqBrCount = 0;
            } else if (openingSqBrCount > 0 && conditionArray.get(i).getIsSqBracket()
                    && conditionArray.get(i).getFilterConditionStr() == "[") {
                openingSqBrCount++;
            } else if (openingSqBrCount > 0 && openingSqBrCount > closingSqBrCount + 1
                    && conditionArray.get(i).getIsSqBracket() && conditionArray.get(i).getFilterConditionStr() == "]") {
                closingSqBrCount++;
            }
        }
    }
    
    public static ArrayList<TreeNode<DirectoryInfo>> filterFilesByCondition(ArrayList<TreeNode<DirectoryInfo>> dirTreesList, 
            TreeNode<FilterConditionElement> filterTree) {
//        Create new dirTreesList
        ArrayList<TreeNode<DirectoryInfo>> toReturnDirTreesList = new ArrayList<TreeNode<DirectoryInfo>>();
        
//        If root of filterTree exists
        if (filterTree != null) {
            if (filterTree.data.getIsOperator()) {
//                System.out.println("*** root of filterTree is operator ***");
                List<TreeNode<FilterConditionElement>> childrenList = null;
                childrenList = filterTree.children;
//                If children nodes of root node exists, build ArrayList 
//                        of trees of directories for left and right children nodes
                if (childrenList != null) {
                    ArrayList<TreeNode<DirectoryInfo>> leftDirTreesList
                            = filterFilesByCondition(dirTreesList, childrenList.get(0));
                    ArrayList<TreeNode<DirectoryInfo>> rightDirTreesList
                            = filterFilesByCondition(dirTreesList, childrenList.get(childrenList.size() - 1));

                    for (int i = 0; i < leftDirTreesList.size(); i++) {
//                        Pass directories trees recursively
                        passDirTrees(leftDirTreesList.get(i), 
                                rightDirTreesList.get(i), 
                                dirTreesList.get(i), 
                                filterTree);
                    }
                    
//                    Copy rightDirTreesList to toReturnDirTreesList
                    toReturnDirTreesList = (ArrayList<TreeNode<DirectoryInfo>>)rightDirTreesList.clone();
                }
//            If root of filterTree is keyword
            } else {
                
                for (TreeNode<DirectoryInfo> dirTreesListTree : dirTreesList) {
                    toReturnDirTreesList.add(copyDirTree(dirTreesListTree));
                }
                for (int i = 0; i < toReturnDirTreesList.size(); i++) {
//                    Pass directories trees recursively if a keyword in condition
                    passDirTreesIfKeyword(toReturnDirTreesList.get(i), filterTree);
                }
            }
        }
        
        return toReturnDirTreesList;
    }
    
    public static void passDirTrees(TreeNode<DirectoryInfo> leftDirTree, 
            TreeNode<DirectoryInfo> rightDirTree, 
            TreeNode<DirectoryInfo> origDirTree, 
            TreeNode<FilterConditionElement> rootNode) {
        ArrayList<FileInfo> leftDirTreeFileList = leftDirTree.data.getFilesList();
        ArrayList<FileInfo> rightDirTreeFileList = rightDirTree.data.getFilesList();
        ArrayList<FileInfo> origDirTreeFileList = origDirTree.data.getFilesList();
//        Get lists of file's paths for both file's lists
        Set<String> leftDirTreeFilePathSet = new HashSet<String>();
        Set<String> rightDirTreeFilePathSet = new HashSet<String>();
        
        for (FileInfo fInfo : leftDirTreeFileList) {
            leftDirTreeFilePathSet.add(fInfo.getFilePath());
        }
        for (FileInfo fInfo : rightDirTreeFileList) {
            rightDirTreeFilePathSet.add(fInfo.getFilePath());
        }
        
        Set<String> rightCopiedDirTreeFilePathSet = new HashSet<String>(rightDirTreeFilePathSet);
        
//        If 'or', get union set
        if (rootNode.data.getFilterConditionStr().equals("or")) {
//            System.out.println("*** If or ***");
            rightDirTreeFilePathSet.addAll(leftDirTreeFilePathSet);
            
//            - If 'no' attribute is true, get list of file's paths 
//            for original file's list and then invert set (union)
//            - If 'no', remove items from 'right' FileInfo list and 
//            add FileInfo items from original list there 
//            if their file's paths are in set, 
            if (rootNode.data.getNoProperty()) {
//                System.out.println("*** If no ***");
                Set<String> origDirTreeFilePathSet = new HashSet<String>();
                for (FileInfo fInfo : origDirTreeFileList) {
                    origDirTreeFilePathSet.add(fInfo.getFilePath());
                }
                origDirTreeFilePathSet.removeAll(rightDirTreeFilePathSet);

                rightDirTreeFileList.clear();
                for (FileInfo fInfo : origDirTreeFileList) {
                    if (origDirTreeFilePathSet.contains(fInfo.getFilePath())) {
                        rightDirTreeFileList.add(fInfo);
                    }
                }
            }
//            - else get FileInfo items from 'left' list and add them 
//            to 'right' list if file's paths of these FileInfo items 
//            are in set (union) and not in 'right' path's list
            else {
//                System.out.println("*** If yes ***");
                for (FileInfo fInfo : leftDirTreeFileList) {
                    if (rightDirTreeFilePathSet.contains(fInfo.getFilePath())
                            && !rightCopiedDirTreeFilePathSet.contains(fInfo.getFilePath())) {
                        rightDirTreeFileList.add(fInfo);
                    }
                }
            }
        }
//        else (if 'and')
        else {
//            System.out.println("*** If and ***");
//            Intersection
            rightDirTreeFilePathSet.retainAll(leftDirTreeFilePathSet);
            if (rootNode.data.getNoProperty()) {
//                System.out.println("*** If no ***");
                rightDirTreeFileList.clear();
                for (FileInfo fInfo : origDirTreeFileList) {
                    if (!rightDirTreeFilePathSet.contains(fInfo.getFilePath())) {
                        rightDirTreeFileList.add(fInfo);
                    }
                }
            } else {
//                System.out.println("*** If yes ***");
                
                Iterator<FileInfo> rightDirTreeFileIter = rightDirTreeFileList.iterator();
                while (rightDirTreeFileIter.hasNext()) {
                    FileInfo fInfo = rightDirTreeFileIter.next();
                    if (!rightDirTreeFilePathSet.contains(fInfo.getFilePath())) {
                        rightDirTreeFileIter.remove();
                    }
                }
            }
        }
        
//        Write 'right' list to rightDirTree.data
        rightDirTree.data.setFilesList(rightDirTreeFileList);
        
//        Pass directories trees recursively
        for (int i = 0; i < rightDirTree.children.size(); i++) {
            passDirTrees(leftDirTree.children.get(i), 
                    rightDirTree.children.get(i), 
                    origDirTree.children.get(i), 
                    rootNode);
        }
    }
    
    public static void passDirTreesIfKeyword(TreeNode<DirectoryInfo> dirTree, 
            TreeNode<FilterConditionElement> rootNode) {
        ArrayList<FileInfo> dirTreeFileList = dirTree.data.getFilesList();
        
        System.out.println("*** passDirTreesIfKeyword ***");
        System.out.println("*** Print dirTree ***");
        printDirectoryTree(dirTree);
        System.out.println("*** Print filter tree ***");
        printFilterConditionTree(rootNode);
        System.out.println("*** /Print filter tree ***");

        Iterator<FileInfo> dirTreeFileIter = dirTreeFileList.iterator();
        while (dirTreeFileIter.hasNext()) {
            FileInfo fInfo = dirTreeFileIter.next();
            Boolean fContKeyw = 
                    fInfo.getFileKeywordsSet().contains(rootNode.data.getFilterConditionStr());
            if (rootNode.data.getNoProperty() == false && fContKeyw == false)
                dirTreeFileIter.remove();
            else if (rootNode.data.getNoProperty() && fContKeyw)
                dirTreeFileIter.remove();
        }
        
        System.out.println("*** Write dirTreeFileList to dirTree.data ***");
//        Write dirTreeFileList to dirTree.data
        dirTree.data.setFilesList(dirTreeFileList);
        
        System.out.println("*** Print dirTree.data ***");
        System.out.println(dirTree.data.getDirectoryPath());
        for (FileInfo fInfo : dirTree.data.getFilesList()) {
            System.out.println("<file> " + fInfo.getFilePath());
        }
        System.out.println("*** /Print dirTree.data ***");
        
//        Pass directories' trees recursively
        for (int i = 0; i < dirTree.children.size(); i++) {
            passDirTreesIfKeyword(dirTree.children.get(i), rootNode);
        }
    }
    
    public static TreeNode<DirectoryInfo> copyDirTree (TreeNode<DirectoryInfo> dirTree) {
        String dirPath = new String(dirTree.data.getDirectoryPath());
        ArrayList<FileInfo> dirFilesList = new ArrayList<>();
        for (FileInfo fInfo : dirTree.data.getFilesList()) {
            FileInfo newFileInfo = new FileInfo(fInfo.getFilePath(), fInfo.getFileMetadata(), fInfo.getFileKeywordsSet());
            dirFilesList.add(newFileInfo);
        }
        DirectoryInfo dirInfo = new DirectoryInfo(dirPath, dirFilesList);
        TreeNode<DirectoryInfo> copiedDirTree = new TreeNode<DirectoryInfo>(dirInfo);
        for (TreeNode<DirectoryInfo> childDirTree : dirTree.children) {
            copyDirTreePrivate(childDirTree, copiedDirTree);
        }
        
        return copiedDirTree;
    }
    
    private static void copyDirTreePrivate (TreeNode<DirectoryInfo> childDirTree, 
            TreeNode<DirectoryInfo> copiedDirTree) {
        String dirPath = new String(childDirTree.data.getDirectoryPath());
        ArrayList<FileInfo> dirFilesList = new ArrayList<>();
        for (FileInfo fInfo : childDirTree.data.getFilesList()) {
            FileInfo newFileInfo = new FileInfo(fInfo.getFilePath(), fInfo.getFileMetadata(), fInfo.getFileKeywordsSet());
            dirFilesList.add(newFileInfo);
        }
        DirectoryInfo dirInfo = new DirectoryInfo(dirPath, dirFilesList);
        TreeNode<DirectoryInfo> childCopiedDirTree = copiedDirTree.addChild(dirInfo);
        for (TreeNode<DirectoryInfo> childChildDirTree : childDirTree.children) {
            copyDirTreePrivate(childChildDirTree, childCopiedDirTree);
        }
    }
}

