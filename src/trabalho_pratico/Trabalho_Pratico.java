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
       //retiraNomeWiki("./escritores/AlexandreHerculano_Wiki.txt");
       //retiraDataNascimentoWiki("./escritores/JorgeAmado_Wiki.txt");
       //retiraNacionalidadeWiki("./escritores/JamesJoyce_Wiki.txt");
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
            //getWikiFile(autor);
            //getWookFile(autor);
            retiraNomeWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ", ""));
            retiraDataNascimentoWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ",""));
            retiraNacionalidadeWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ",""));
            retiraDataMorteWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ",""));
            retiraOcupWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ",""));
            retiraPremWiki("./escritores/"+autor.concat("_Wiki.txt").replace(" ",""));
            System.out.println("\n");
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
        
        String er="<p><b>([a-zA-Zàáâāéíóú',.\\s]*)</b>[\\s,]";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                System.out.println("Nome: "+m.group(1));
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
        
        String er="title=\"([0-9a-zA-Zç\\s]+)\">[0-9a-zA-Zç\\s]+</a> de <a href=\"/wiki/([0-9]{4})\" title=\"[0-9]{4}\">[0-9]{4}</a>";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                data_nasc = m.group(1)+ " de " + m.group(2);
                System.out.println("Data Nascimento: "+data_nasc);
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
        
        String er1=">Nacionalidade</td>";  
        String er2="<a href=\"/wiki/[a-zA-Zàáâāéíóúê',()\\s-_%0-9]*\" title=\"[a-zA-Zàáâāéíóúê',()%\\s-_]*\">([a-zA-Zàáâāéíóúê',-_%()\\s]*)</a>";
        Pattern p1 = Pattern.compile(er1);
        Pattern p2 = Pattern.compile(er2);
        Matcher m;
        Matcher m2;
        String nacionalidade;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                nacionalidade = input.nextLine();
                m2=p2.matcher(nacionalidade);
                if(m2.find()){
                    System.out.println("Nacionalidade: "+m2.group(1));
                    break;
                }
                
            }
        }
    }
    
    public static void retiraDataMorteWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha;
        String morte;
        String data_morte;
        String er1=">Morte</td>";  
        String er2="title=\"([0-9a-zA-Zç\\s]+)\">[0-9a-zA-Zç\\s]+</a> de <a href=\"/wiki/([0-9]{4})\" title=\"[0-9]{4}\">[0-9]{4}</a>";
        Pattern p1 = Pattern.compile(er1);
        Pattern p2 = Pattern.compile(er2);
        Matcher m;
        Matcher m2;
       
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                morte = input.nextLine();
                m2=p2.matcher(morte);
                if(m2.find())
                {
                    data_morte = m2.group(1)+ " de " + m2.group(2);
                    System.out.println("Data de morte: "+data_morte);
                    break;
                }
                
            }
        }
    }
    
    public static void retiraOcupWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String ocup;
        String linha;
        int group=1;
        String er1=">Ocupação</td>";  
        String er2=">([a-zA-Zàáâāéíóúê',()\\s-_]*)[</a>]?</td>|<a href=\"/wiki/[a-zA-Z]*\"[ class=\"mw\\-redirect\"]*title=\"[a-zA-Z]*\">([a-zA-Z]*)</a>";
        Pattern p1 = Pattern.compile(er1);
        Pattern p2 = Pattern.compile(er2);
        Matcher m;
        Matcher m2;
        String ocupacao;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                ocup = input.nextLine();
                m2=p2.matcher(ocup);
                while(m2.find())
                {
                    ocupacao = m2.group(1);
                    if(ocupacao==null)
                    {    
                        ocupacao = m2.group(2);
                    } 
                    System.out.println("Ocupacao: "+ocupacao);
                    break;
                }
                
            }
        }
    }
    public static void retiraPremWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String prem, linha, premios;
        String er1=">Prémios</td>|>Prêmios</td>";
        String er2="title=\"[a-zA-Zàáâāéíóúêõ',.()\\s/-_]*\">([a-zA-Zàáâāéíóúêõ'.,()\\s/-_]*)</a> ([(0-9),\\s]*)<br />|title=\"(Prémio [a-zA-Zàáâāéíóúê]*)\">";
        Pattern p1=Pattern.compile(er1);
        Pattern p2=Pattern.compile(er2);
        Matcher m, m2;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                while(input.hasNextLine())
                {
                    prem = input.nextLine();
                    m2=p2.matcher(prem);
                    while(m2.find())
                    {
                        premios = m2.group(1);
                        if(premios==null)
                        {
                            premios=m2.group(3);
                            System.out.println("grupo3");
                            System.out.println("Premios: "+premios);
                            break;
                        }
                        System.out.println("Premios: "+premios);
                    }
                }
                break;
            }
        }
    }
    
    public static void retiraImgWiki(String ficheiro) throws FileNotFoundException
    {
        
    }
 }
