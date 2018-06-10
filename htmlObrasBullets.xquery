<html>
<body>
    <h1>Lista de Obras</h1>
    <ul>
    {
    for $a in doc("livros.xml")/Catalogo/livro
    order by $a/cod_autor
    return <li>Titulo:{$a/titulo}
                <ul>
                    <li> Código Autor: {$a/cod_autor}</li>
                    <li> Editora : {$a/editora}</li>
                    <li> Numero de Páginas {$a/editora}</li>
                    <li> Preco: {$a/preco}</li>
                </ul>
           </li>
    }
    </ul>

</body>
</html>

