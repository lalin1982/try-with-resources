<p style="text-align: justify;">Java 7 añade nuevas características bastante interesantes como son mejoras en la JVM, posibilidad del uso de <a href="http://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html" title="String Switch" target="_blank">String en los switch</a>, formatos <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/language/binary-literals.html" title="Binary numbers" target="_blank">binarios </a>de números... Entre estas nuevas características se encuentra la sentencia <em>try-with-resources</em> o <em>ARM (Automatic Resource Management)</em>. Hasta ahora el manejo de los recursos, como ficheros, sockets, streams o conexiones a base de datos, debía ser realizado por el los programador, con los posibles errores que ésto puede originar. Con esta nueva sentencia, la JVM es la encargada de manejar todos los recursos que sean declarados dentro de la sentencia <em>try-with-resources</em>.</p> 

<h4><strong><span style="color: #808080;">Antes de Java 7</span></strong></h4>
<p style="text-align: justify;">Antes de Java 7 los recursos se solían manejar dentro de un bloque <em>try-catch-finally</em>, de forma que dentro del bloque <em>try </em>se utilizaban los recursos y en el bloque <em>finally </em>se cerraban todos los recursos abiertos. Ésto podía causar problemas de memoria y de rendimiento cuando se olvidaba cerrar estos recursos. Un ejemplo de esta forma de manejar los recursos sería:</p><!--more-->
<pre>
  <code>
public static void main(String[] args) {
 
    BufferedReader buffer = null;
    try {
        String linea;
        buffer = new BufferedReader(new FileReader("C:\\test.txt"));
        while ((linea = buffer.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
	    if (buffer != null) {
                buffer.close();
            }
        } catch (IOException ex) {
              ex.printStackTrace();
        }
    }
}
</code>
</pre>

<h4><strong><span style="color: #808080;">Uso del ARM</span></strong></h4>
<p style="text-align: justify;">Ahora si se utiliza la sentencia <em>try-with-resources</em> nos quedaría el siguiente método:</p>
<pre>
  <code>
public static void main(String[] args) {
 
    try (BufferedReader buffer = new BufferedReader(new FileReader("C:\\test.txt"))){
        String linea;
        while ((linea = buffer.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }   
 }
</code>
</pre>

<p style="text-align: justify;">Como se puede observar el código es más sencillo y más fácil de leer, reduciendo bastante el número de líneas y también se elimina el bloque <em>finally </em>para el cierre de recursos.</p>

<p style="text-align: justify;">En este ejemplo, el recurso declarado en la sentencia <em>try-with-resources</em> es un BufferedReader. La declaración aparece entre paréntesis a continuación del <em>try</em>, de esta manera la instancia declarada en la sentencia <em>try-with-resource</em> será cerrada automáticamente cuando el bloque de sentencias del try sean ejecutadas completamente o cuando se produzca una excepción.</p> 

<p style="text-align: justify;">de la sentencia se pueden declarar tantos recursos como sea necesario, cerrándose de manera inversa a como han sido declarados:</p>
<pre>
  <code>
public void readEmployees(Connection con){
    String sql = "Select * from Employee";
    
    try (Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)){
       //Código...   
    } catch (SQLException sqe) {
       sqe.printStackTrace();
    }
 }
</code>
</pre>

<p style="text-align: justify;">En este caso, primero se cerraría el ResultSet y a continuación el Statement.</p>

<h4><strong><span style="color: #808080;">¿Qué hay nuevo en el API?</span></strong></h4>
<p style="text-align: justify;">Para permitir el uso del ARM se ha añadido el interfaz <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/AutoCloseable.html" target="_blank">java.lang.AutoCloseable</a> en el API, este interfaz contiene únicamente la definición del método <em>close()</em>.</p>
<pre>
  <code>
public interface AutoClosable {
    public void close() throws Exception;
}
</code>
</pre>

<p style="text-align: justify;">Este interfaz es padre del interfaz <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Closeable.html" title="Closable" target="_blank">java.io.Closeable</a> del que heredan todos los recursos de entrada y salida. El método <em>close()</em> es que se invoca para el cierra todos los recursos incluidos en la cabecera de la sentencia <em>try</em>, llamándose automáticamente una vez finalizado el bloque <em>try</em> o después de producirse una excepción. De manera que todos los recursos declarados en la cabecera del bloque <em>try </em>deberán implementar <em>AutoClosable </em>o <em>Closable</em>. Si se declara algún recurso que no herede de <em>AutoClosable </em>se producirá un error de compilación.</p>

<p style="text-align: justify;">Aunque como se indica en el API, la implementación de este método no es obligatoria, aunque si es aconsejable para un mejor manejo de los recursos. De esta manera, cada una de las clases que hereden de <em>Closeable </em>se encargara de implementar específicamente el método <em>close()</em>. Por ejemplo, la implementación de este método en la clase <em>BufferReader </em>sería:</p>
<pre>
  <code>
public void close() throws IOException {
   synchronized (lock) {
      if (in == null)
         return;
      in.close();
      in = null;
      cb = null;
   }
}
</code>
</pre>

<h4><strong><span style="color: #808080;">Uso con JDBC</span></strong></h4>
<p style="text-align: justify;">Otro posible uso es en el manejo de base de datos con <em>JDBC</em>. Antes de java 7, todos los recursos debían ser cerrados manualmente una vez finalizado su uso, dando la posibilidad de olvidar cerrar alguno y producir graves errores en la base de datos.</p>
<pre>
  <code>
Connection conn = null;
Statement stmt = null;
ResultSet rs = null;
try {
    conn = DriverManager.getConnection("jdbc:h2:mem:test", "", "");
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.USERS");
    while (rs.next()) {
        System.out.println(rs.getString(1));
    }
} catch (SQLException e) {
    e.getCause();
} finally {
    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException e) {
            e.getCause();
        }
    }
    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.getCause();
        }
    }
    if (conn != null) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.getCause();
        }
    }
}
</code>
</pre>

<p style="text-align: justify;">Ahora con la nueva sintaxis, el mismo código quedaría de la siguiente manera:</p>
<pre>
  <code>
try(Connection conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.USERS")) {
        while (rs.next()) {
           System.out.println(rs.getString(1));
        }
} catch (SQLException e) {
    e.getCause();
}
</code>
</pre>

<p style="text-align: justify;">Las variables <em>connection</em>, <em>prepared statement</em> y <em>result set</em> son creadas en la sentencia <em>try </em>y después de que se iteren sobre todos los resultados, cuando el bloque termina, la JVM automáticamente cierra todos los recursos.</p> 

<h4><strong><span style="color: #808080;">Manejo de excepciones</span></strong></h4>
<p style="text-align: justify;">El manejo de excepciones es un poco diferente entre <em>try-catch-finally</em> y <em>try-with-resources</em>. Si las excepciones son lanzadas en el bloque <em>try </em>y en el <em>finally</em>, en un <em>try-catch-finally</em> se devuelve la excepción lanzada en el bloque <em>finally </em>mientras que si en una sentencia <em>try-with-resources</em> el metodo devuelve la excepción lanzada por el bloque <em>try</em>. Unos ejemplos podrian ser:</p>
<pre>
  <code>
public class ExceptionARM{
 
    public static void main(String[] args) throws Exception {
        try {
            tryWithResourceException();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            normalTryException();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
 
    private static void normalTryException() throws Exception {
        MyResource mr = null;
        try {
            mr = new MyResource();
            System.out.println("MyResource created in try block");
            if (true)
                throw new Exception("Exception in try");
        } finally {
            if (mr != null)
                mr.close();
        }
 
    }
 
    private static void tryWithResourceException() throws Exception {
        try (MyResource mr = new MyResource()) {
            System.out.println("MyResource created in try-with-resources");
            if (true)
                throw new Exception("Exception in try");
        }
    }
 
    static class MyResource implements AutoCloseable {
 
        @Override
        public void close() throws Exception {
            System.out.println("Closing MyResource");
            throw new Exception("Exception in Closing");
        }
 
    }
}
</code>
</pre>

<p style="text-align: justify;">La salida del programa será:</p>
<pre>
  <code>
MyResource created in try-with-resources
Closing MyResource
Exception in try
MyResource created in try block
Closing MyResource
Exception in Closing
</code>
</pre>

<p style="text-align: justify;">Esto se debe a que la excepción producida en el método <em>close()</em> es ocultada en la primera excepción. A este tipo de excepciones se le llama "suppressed exceptions". Se pueden recuperar con el método <em>getSuppressed()</em> de la clase Throwable.</p>

<h4><strong><span style="color: #808080;">Creación de un recuros AutoClosable</span></strong></h4>
<p style="text-align: justify;">Para crear un recurso que pueda ser utilizado en una sentencia <em>try-with-resources</em>, necesitamos implementar el interfaz <em>AutoCloseable</em>. Como se ha dicho anteriormente el interfaz <em>AutoClosable</em> únicamente tiene un método <em>close()</em>. Una simple implementación podría ser:</p>
<pre>
  <code>
public class Adult implements AutoCloseable{

    public Adult() {
        System.out.println("Me levanto por la mañana");
    }

    public void work() {
        System.out.println("Hago mi trabajo");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Me voy a dormir");
    }
}

public class Car implements AutoCloseable{

    public Car() {
        System.out.println("Coche aparcado en el garaje");
    }

    public void drive() {
        System.out.println("Voy al trabajo con el coche");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Aparco el coche en el garaje");
    }
}

public class ExampleARM {

    public static void main(String[] args) {
        try (Adult adult = new Adult(); Car car = new Car()) {
            car.drive();
            adult.work();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("Fin del día.");
        }

    }
}
</code>
</pre>

<p style="text-align: justify;">La salida del ejemplo será:</p>
<pre>
  <code>
Me levanto por la mañana
Coche aparcado en el garaje
Voy al trabajo con el coche
Hago mi trabajo
Aparco el coche en el garaje
Me voy a dormir
Fin del día.
</code>
</pre>

<h4><strong><span style="color: #808080;">Java Language Specification</span></strong></h4>
<p style="text-align: justify;">Como se dice en <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.20.3" title="Java Language Specification" target="_blank">Java Language Specification</a>, la estructura:</p>
<pre>
  <code>
try (VariableModifiersopt R Identifier = Expression ...)
    Block
</code>
</pre>

<p style="text-align: justify;">Es convertido a la siguiente estructura:</p>
<pre>
  <code>
{
    final VariableModifiers_minus_final R Identifier = Expression;
    Throwable #primaryExc = null;
 
    try ResourceSpecification_tail
        Block
    catch (Throwable #t) {
        #primaryExc = #t;
        throw #t;
    } finally {
        if (Identifier != null) {
            if (#primaryExc != null) {
                try {
                    Identifier.close();
                } catch (Throwable #suppressedExc) {
                    #primaryExc.addSuppressed(#suppressedExc);
                }
            } else {
                Identifier.close();
            }
        }
    }
}
</code>
</pre>
<p style="text-align: justify;">Como se puede ver al final tenemos la misma estructura que se ha utilizado siempre en un bloque <em>try-catch-finally</em>, pero con una sintaxis más sencilla y menos propensa a cometer errores en el uso de recursos.</p>

Enlace | <a href="http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html" title="The try-with-resources Statement" target="_blank">The try-with-resources Statement</a>
