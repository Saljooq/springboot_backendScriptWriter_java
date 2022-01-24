import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class springWriter {

    public static void main (String[] args) throws IOException {
        Scanner scnr = new Scanner(System.in);

        System.out.println("Enter your package name for instance project.foo.test");
        String packageName = scnr.next();

        String path = "src/main/java/"+replaceDotsWithSlash(packageName)+"/";
        File modelDir = new File(path+"model");
        File repoDir = new File(path+"repository");
        File controllerDir = new File(path+"controller");
        if (!modelDir.exists()){
            modelDir.mkdirs();
        }
        if (!repoDir.exists()){
            repoDir.mkdirs();
        }
        if (!controllerDir.exists()){
            controllerDir.mkdirs();
        }



        System.out.println("type the model you want to create (it should start with uppercase and \n" +
                "shouldn't have any spaces), it will have an id built in");

        String model1 = scnr.next();
        String model1_l = lowerCapOfFirstLetter(model1);

        System.out.println("Next you can add variables if you want here, just right it in the form 'String name' or \n" +
                "'int numOfTrips'\n" +
                "When you're done just type DONE");
        String newWord = scnr.next();

        ArrayList<String> variableTypes =  new ArrayList<>();
        ArrayList<String> variableNames =  new ArrayList<>();


        // adding int id
        variableTypes.add("int");
        variableNames.add("id");

        while (!newWord.equals("DONE")){
            variableTypes.add(newWord);
            newWord = scnr.next();
            variableNames.add(newWord);
            newWord = scnr.next();

        }



        String model1Lower = model1.toCharArray()[0] + 'a' - 'A' + model1.substring(1);

        FileWriter wr = new FileWriter(path+"model/"+model1+".java");

        String output = "" +
                "package "+packageName+".model;\n" +
                "\n" +
                "import javax.persistence.Entity;\n" +
                "import javax.persistence.GeneratedValue;\n" +
                "import javax.persistence.GenerationType;\n" +
                "import javax.persistence.Id;\n" +
                "\n" +
                "@Entity\n" +
                "public class "+model1+" {\n" +
                "\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n";
        for (int i = 0; i < variableNames.size(); i++){
            output += "    private "+ variableTypes.get(i) + " " + variableNames.get(i)+";\n";
        }
        output += "\n" +
                "    public "+model1+"() {\n" +
                "    }\n" +
                "\n";
        for (int i = 0; i < variableNames.size(); i++){
            output += "    public "+ variableTypes.get(i) + " get"+ capitaliseFirstLetter(variableNames.get(i)) +"() {\n" +
                    "        return "+variableNames.get(i)+";\n" +
                    "    }\n" +
                    "\n" +
                    "    public void set"+ capitaliseFirstLetter(variableNames.get(i))+"("+variableTypes.get(i)+" "+ variableNames.get(i)+") {\n" +
                    "        this."+ variableNames.get(i)+" = "+variableNames.get(i)+";\n" +
                    "    }\n";
        }

        output+= "}";


        wr.write(output);
        wr.close();


        wr = new FileWriter(path+"repository/"+model1+"Repository.java");

        output = "" +
                "package "+packageName+".repository;\n" +
                "\n" +
                "import "+packageName+".model."+ model1+";\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "\n" +
                "public interface "+model1+"Repository extends JpaRepository<"+model1+", Integer> {\n" +
                "}\n";

        wr.write(output);
        wr.close();


        wr = new FileWriter(path+"controller/"+model1+"Controller.java");

        output = "" +
                "package "+packageName+".controller;\n" +
                "\n" +
                "import "+packageName+".model."+ model1+";\n" +
                "import "+packageName+".repository."+ model1+"Repository;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "import java.util.Optional;" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "@RestController\n" +
                "public class "+ model1+"Controller {\n" +
                "\n" +
                "\n" +
                "    @Autowired\n" +
                "    "+ model1+"Repository "+ model1_l+"Repository;\n" +
                "\n" +
                "\n" +
                "    @GetMapping(\""+model1_l+"/all\")\n" +
                "    List<"+model1+"> GetAll"+model1+"(){\n" +
                "        return "+model1_l+"Repository.findAll();\n" +
                "    }\n" +
                "\n" +
                "    @GetMapping(\""+model1_l+"/{id}\")\n" +
                "    "+model1+" Get"+model1+"ById(@PathVariable int id){\n" +
                "    Optional<"+model1+"> t = "+model1_l+"Repository.findById(id);\n" +
                "        if (t.isPresent())\n" +
                "            return t.get();\n" +
                "        else\n" +
                "            return null;" +
                "    }\n" +
                "\n" +
                "    \n" +
                "    @DeleteMapping(\""+model1_l+"\")\n" +
                "    "+model1+" Delete"+model1+"(@RequestBody "+model1+" t){\n" +
                "        "+model1_l+"Repository.delete(t);\n" +
                "        return t;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @PostMapping(\""+model1_l+"\")\n" +
                "    "+model1+" Post"+model1+"(@RequestBody "+model1+" n){\n" +
                "        "+model1_l+"Repository.save(n);\n" +
                "        return n;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "}\n";

        wr.write(output);
        wr.close();



        scnr.close();




        System.out.println(capitaliseFirstLetter("Hello World"));
    }

    static String capitaliseFirstLetter(String in){
        if (in.toCharArray()[0] >= 'a') {
            int firstInt = in.toCharArray()[0] - 'a' + 'A';
            char first = (char) firstInt;
            return first + in.substring(1);
        }
        else
            return in;
    }
    static String lowerCapOfFirstLetter(String in){
        if (in.toCharArray()[0] <= 'Z') {
            int firstInt = in.toCharArray()[0] + 'a' - 'A';
            char first = (char) firstInt;
            return first + in.substring(1);
        }
        else
            return in;
    }
    static String replaceDotsWithSlash(String in){
        char[] array = in.toCharArray();
        for (int i = 0; i < array.length; i++){
            if (array[i] == '.') {
                array[i] = '/';
            }
        }
        return String.copyValueOf(array);
    }
}
