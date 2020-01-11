package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MvpCreateAction extends AnAction {

    private Project project;
    //包名
    private String packageName = "";
    //模块名称
    private String mModuleName;
    //创建的类型
    private String mType = CreateDialog.FRAGMENT;

    private String xmlName = "view";

    private enum CodeType {
        Activity, Fragment, Contract, Presenter, BaseView, BasePresenter, BaseActivity, BaseFragment
    }


    public MvpCreateAction(){

    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        project = e.getData(PlatformDataKeys.PROJECT);
        packageName = getPackageName();
        init();
        refreshProject(e);

    }

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     *
     * @return 包名
     */
    private String getPackageName() {
        String package_name = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(project.getBasePath() + "/App/src/main/AndroidManifest.xml");

            NodeList nodeList = doc.getElementsByTagName("manifest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                package_name = element.getAttribute("package");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return package_name;
    }

    /**
     * 初始化Dialog
     */
    private void init() {
        CreateDialog myDialog = new CreateDialog((type, moduleName) -> {
            mModuleName = moduleName;
            mType = type;
            createClassFiles();
            Messages.showInfoMessage(project, "create mvp code success", "title");
        });
        myDialog.setVisible(true);
    }

    /**
     * 生成类文件
     */
    private void createClassFiles() {
        if (mType.equals(CreateDialog.ACTIVITY)) {
            createClassFile(CodeType.Activity);
            xmlName = "activity_" + mModuleName.toLowerCase() + ".xml";
            createXmlFile(xmlName);
        }
        if (mType.equals(CreateDialog.FRAGMENT)) {
            createClassFile(CodeType.Fragment);
            xmlName = "fragment_" + mModuleName.toLowerCase() + ".xml";
            createXmlFile(xmlName);
        }
        if (mType.equals(CreateDialog.ALL)) {
            createClassFile(CodeType.Activity);
            createClassFile(CodeType.Fragment);
            createXmlFile("activity_" + mModuleName.toLowerCase() + ".xml");
            createXmlFile("fragment_" + mModuleName.toLowerCase() + ".xml");
        }
        //架构改动  攒不生成
     /*   createBaseClassFile(CodeType.BaseView);
        createBaseClassFile(CodeType.BasePresenter);
        createBaseClassFile(CodeType.BaseActivity);
        createBaseClassFile(CodeType.BaseFragment);*/

        createClassFile(CodeType.Contract);
        createClassFile(CodeType.Presenter);
    }

    /**
     * 生成布局文件
     */
    private void createXmlFile(String fileName) {
        String content = dealTemplateContent(ReadTemplateFile("Templatexml.txt"));
        String appPath = project.getBasePath() + "/App/src/main/res/layout/";
        writeToFile(content, appPath, fileName);
    }

    /**
     * 生成mvp框架代码
     *
     * @param codeType
     */
    private void createClassFile(CodeType codeType) {
        switch (codeType) {
            case Activity:
                createFile("TemplateActivity.txt", toUpperCaseFirstOne(mModuleName) + "Activity.java");
                break;
            case Fragment:
                createFile("TemplateFragment.txt", toUpperCaseFirstOne(mModuleName) + "Fragment.java");
                break;
            case Contract:
                createFile("TemplateContract.txt", toUpperCaseFirstOne(mModuleName) + "Contract.java");
                break;
            case Presenter:
                createFile("TemplatePresenter.txt", toUpperCaseFirstOne(mModuleName) + "Presenter.java");
                break;
        }
    }

    private void createFile(String fileName, String CreateName) {
        String appPath = getAppPath()+ "/ui/"+mModuleName.toLowerCase();
        if (mType.equals(CreateDialog.ACTIVITY)) {
             appPath = getAppPath()+ "/ui/activity/"+mModuleName.toLowerCase();
        }
        if (mType.equals(CreateDialog.FRAGMENT)) {
            appPath = getAppPath()+ "/ui/fragment/"+mModuleName.toLowerCase();
        }

        String content = dealTemplateContent(ReadTemplateFile(fileName));
        writeToFile(content, appPath , CreateName);
    }


    /**
     * 生成base类
     *
     * @param codeType
     */
    private void createBaseClassFile(CodeType codeType) {
        String fileName = "";
        String content = "";
        String basePath = getAppPath() + "/base/";
        switch (codeType) {
            case BaseView:
                if (!new File(basePath + "BaseView.java").exists()) {
                    fileName = "BaseView.txt";
                    content = ReadTemplateFile(fileName);
                    content = dealTemplateContent(content);
                    writeToFile(content, basePath, "BaseView.java");
                }
                break;
            case BasePresenter:
                if (!new File(basePath + "BasePresenter.java").exists()) {
                    fileName = "BasePresenter.txt";
                    content = ReadTemplateFile(fileName);
                    content = dealTemplateContent(content);
                    writeToFile(content, basePath, "BasePresenter.java");
                }
                break;
            case BaseActivity:
                if (!new File(basePath + "BaseActivity.java").exists()) {
                    fileName = "BaseActivity.txt";
                    content = ReadTemplateFile(fileName);
                    content = dealTemplateContent(content);
                    writeToFile(content, basePath, "BaseActivity.java");
                }
                break;
            case BaseFragment:
                if (!new File(basePath + "BaseFragment.java").exists()) {
                    fileName = "BaseFragment.txt";
                    content = ReadTemplateFile(fileName);
                    content = dealTemplateContent(content);
                    writeToFile(content, basePath, "BaseFragment.java");
                }
                break;
        }
    }


    /**
     * 获取包名文件路径
     *
     * @return
     */
    private String getAppPath() {
        String packagePath = packageName.replace(".", "/");
        String appPath = project.getBasePath() + "/App/src/main/java/" + packagePath ;
        return appPath;
    }

    /**
     * 读取模板文件中的字符内容
     *
     * @param fileName 模板文件名
     * @return
     */
    private String ReadTemplateFile(String fileName) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/Template/" + fileName);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    /**
     * 替换模板中字符
     *
     * @param content
     * @return
     */
    private String dealTemplateContent(String content) {
        content = content.replace("$name", toUpperCaseFirstOne(mModuleName));
        if (content.contains("$rpackagename")){
            content = content.replace("$rpackagename", packageName);
        }
        if (content.contains("$packagename")) {
            if (mType.equals(CreateDialog.ACTIVITY)) {
                content = content.replace("$packagename", packageName + ".ui.activity." + mModuleName.toLowerCase());
            }
            if (mType.equals(CreateDialog.FRAGMENT)) {
                content = content.replace("$packagename", packageName + ".ui.fragment." + mModuleName.toLowerCase());
            }

        }
        if (content.contains("$basepackagename")) {
            content = content.replace("$basepackagename", packageName + ".base");
        }
        if (content.contains("$axmlname")){
            content = content.replace("$axmlname","activity_" + mModuleName.toLowerCase());
        }
        if (content.contains("$fxmlname")){
            content = content.replace("$fxmlname","fragment_" + mModuleName.toLowerCase());
        }
//        content = content.replace("$author", mAuthor);
        if (content.contains("$date")) {
            content = content.replace("$date", getDate());
        }
        return content;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

//    public static void main(String[] args) {
//
//        MvpCreateAction action = new MvpCreateAction();
//        System.out.print(action.ReadTemplateFile("Templatexml.txt"));
//
//
////        String appPath = project.getBasePath() + "/App/src/main/res/layout/";
////        writeToFile(content, appPath, fileName);
//    }


    private byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        byte[] file;
        int len = -1;
        try {
            while ((len = inputStream.read()) != -1) {
                outputStream.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file = outputStream.toByteArray(); // 取内存中保存的数据
            outputStream.close();
            if (inputStream != null)
                inputStream.close();
        }

        return file;
    }


    /**
     * 生成
     *
     * @param content   类中的内容
     * @param classPath 类文件路径
     * @param className 类文件名称
     */
    private void writeToFile(String content, String classPath, String className) {
        try {
            File floder = new File(classPath);
            if (!floder.exists()) {
                floder.mkdirs();
            }

            File file = new File(classPath + "/" + className);
            if (!file.exists()) {
                file.createNewFile();
            }else{
                Messages.showInfoMessage(project, "The file already exists", "title");
                return;
            }

            FileOutputStream fw = new FileOutputStream(file.getAbsoluteFile());
            //BufferedWriter bw = new BufferedWriter(fw);
            fw.write(content.getBytes());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 刷新项目
     *
     * @param e
     */
    private void refreshProject(AnActionEvent e) {
        e.getProject().getBaseDir().refresh(false, true);
    }


}
