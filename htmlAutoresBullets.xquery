<html>
<body>
    <h1>Lista de Autores</h1>
    <ul>
    {
    for $a in doc("autores.xml")/Autores/autor
    order by $a/nome
    return <li>Nome:{$a/nome} Data Nascimento:{$a/data_nasc}</li>
    }
    </ul>
</body>
</html>

