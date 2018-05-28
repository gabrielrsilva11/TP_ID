/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_pratico;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gabriel
 */
public class Trabalho_Pratico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
       buscaAutoresFicheiro("escritores.txt");
       // retiraNomeWiki("AntonioLoboAntunes_Wiki.txt");
       //retiraDataNascimentoWiki("./escritores/JorgeAmado_Wiki.txt");
    }
    
    /*input: autor - nome do autor da qual queremos a pagina
      output: ficheiro de texto com a sua pagina da wikipedia 
    */
    public static void getWikiFile(String autor) throws IOException
    {
       String link = "https://pt.wikipedia.org/wiki/";
       String outputFile = "./escritores/"+autor.concat("_Wiki.txt").replace(" ", "");
       
       HttpRequestFunctions.httpRequest1(link,autor,outputFile);
    }
    
    /*input: autor - nome do autor da qual queremos a pagina
      output: ficheiro de texto com a sua pagina da wook 
    */
    public static void getWookFile(String autor) throws IOException
    {
       String link = "https://www.wook.pt/pesquisa/";
       String outputFile = "./escritores/"+autor.concat("_Wook.txt").replace(" ", "");
       
       HttpRequestFunctions.httpRequest3(link, autor, outputFile);
    }

    /*input: ficheiro - nome do ficheiro de texto onde ir buscar os nomes dos autores
      output: paginas da wiki e da wook para cada autor em formato .txt
    */
    public static void buscaAutoresFicheiro(String nomeFich) throws FileNotFoundException, IOException
    {
        Scanner input = new Scanner(new FileInputStream(nomeFich));
        String autor;
        
        while(input.hasNextLine())
        {
            autor = input.nextLine();
            getWikiFile(autor);
            getWookFile(autor);
            //retiraNomeWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ", ""));
            //retiraDataNascimentoWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ",""));
            retiraNacionalidadeWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ",""));
        }
        input.close();
    }
    
        
    /*input: ficheiro - nome do ficheiro de texto onde ir buscar o nome completo
     output: nome do autor retirado da pagina da wiki
    */
    public static void retiraNomeWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        int found=0;
        String linha;
        //String er="<td style=\"vertical-align: top; text-align: left; padding:4px 4px 4px 0;\">([a-zA-Zàáâāéí'\\s]*)<br />";
        String er="<p><b>([a-zA-Zàáâāéíóú',.\\s]*)</b>[\\s,]";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            while(m.find())
            {
                System.out.println(m.group(1));
                found =1;
            }
            if(found==1)
                break;
        }
        input.close();
    }
    
    public static void retiraDataNascimentoWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        int found=0;
        String linha;
        String data_nasc;
        //String er="title=\"10 de agosto\">10 de agosto</a> de <a href=\"/wiki/1912\"";
        String er="title=\"([1-9a-zA-Zç\\s]+)\">[1-9a-zA-Zç\\s]+</a> de <a href=\"/wiki/([1-9]{4})\" title=\"[1-9]{4}\">[1-9]{4}</a>.*";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            while(m.find())
            {
                data_nasc = m.group(1)+ " de " + m.group(2);
                System.out.println(data_nasc+"\n");
                found =1;
            }
            if(found==1)
                break;
        }
        input.close();
    }
    
    public static void retiraNacionalidadeWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha;
        
        String er="<a href=\"/wiki/[a-zA-Z]\" title=\"([a-zA-Z])*\"</a>,";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            while(m.find())
            {
                System.out.println(m.group(1) + "\n");
            }
        }
    }
}
