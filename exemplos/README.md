## Como usar

* Compile com 

```bash
mvn clean compile
```

* Rode com 

```bash
mvn exec:java -Dexec.mainClass="com.example.Send"
```

Onde deverá ser alterado o mainClass.

* Para rodar com args

```bash
mvn exec:java -Dexec.mainClass=your.package.MainClass -Dexec.args="value1 value2 'argument with spaces'"
```