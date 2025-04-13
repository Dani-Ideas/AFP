# 📘 Implementación de un Autómata Determinista con Pila (PDA)

## 🎯 Propósito

Usar el archivo base en Java proporcionado para **implementar un autómata determinista con pila (PDA)**.  
Se tomó la decisión de **empezar derivando de izquierda a derecha**, con el propósito de detectar primero una cadena que termine en punto y coma (`;`).  
Una vez encontrado el punto y coma, **la derivación cambia de dirección (de derecha a izquierda)** para aplicar los axiomas y consumir la entrada con base en los tokens terminales.

---

## 📊 Tabla de transición con axiomas y tokens

| Axiomas \ Tokens | `a`     | `b`      | `c`    | `d`    |
|------------------|---------|----------|--------|--------|
| `S` (inicial)    | `S := AB` | `S := AB` | error | error |
| `A`              | `A := a` | `A := λ`  | error | error |
| `B`              | error   | `B := bCd` | error | error |
| `C`              | error   | error    | `C := c` | `C := λ` |

---

## 🧠 Ejemplo de derivación (Pila de símbolos)

| Pila         | Entrada | Regla o Acción                             |
|--------------|---------|--------------------------------------------|
| `;S`         | `abcd;` | `S := AB`                                  |
| `;BA`        | `abcd;` | `A := a`                                   |
| `;Ba`        | `abcd;` | Se saca `a` de la pila y la entrada        |
| `;B`         | `bcd;`  | `B := bCd`                                 |
| `;dCb`       | `bcd;`  | Se saca `b` de la pila y la entrada        |
| `;dC`        | `cd;`   | `C := c`                                   |
| `;dc`        | `cd;`   | Se saca `c` de la pila y la entrada        |
| `;d`         | `d;`    | Se saca `d` de la pila y la entrada        |
| `;`          | `;`     | Aceptar                                    |

> 📄 El documento que se analiza como entrada es `archivo.txt`.

---

## 🛠️ ¿Qué se hizo?

- Se tomó como base un archivo compartido en la clase de Compiladores con el profesor **Miguel Ángel Sánchez Sánchez**.
- Se adaptó para cumplir con los requisitos de un **autómata determinista con pila (PDA)**.
- Antes, el flujo era sencillo: leer tokens y analizarlos directamente.
- Ahora, el análisis se divide en dos fases:
    - **Derivación izquierda → derecha**: construye la pila hasta el punto y coma.
    - **Derivación derecha ← izquierda**: analiza la pila y los tokens aplicando axiomas.

---

## 🔄 Comparación del flujo anterior vs. actual

### 🔙 Antes

```plaintext
TXT -> BufferedReader -> Lexico (scanner) -> yylex() -> Tokens(ID, tipo, línea, columna)
        ↑                                        ↓
    creado en main()                        usa Sym para saber qué tipo es
        ↓
    se llama ap.S() -> autómata usa siguienteToken() -> llama a yylex()
```

---

### 🔁 Ahora

```plaintext
archivo.txt → BufferedReader → Lexico (scanner) → yylex() → Tokens(ID, tipo, línea, columna)
       ↑                                         ↓
    usado por                             SiguienteToken.java
       ↓                                         ↓
 Axiomas.java define reglas     ←        Derivador.java recorre la pila y aplica reglas
       ↓
Inicio.java llama S() en Axiomas → apila S y ;, obtiene primer token → llama a analizar()
```

---

## 📁 Estructura del proyecto

```plaintext
├── archivo.txt
├── Conf.txt
├── Documentacion
│   └── README.md
├── jar
│   └── jflex-full-1.7.0.jar
├── out
│   └── production
│       └── sintacticov2
│           └── fes
│               └── aragon
│                   ├── codigo
│                   │   ├── Axiomas.class
│                   │   ├── Derivador.class
│                   │   ├── ErrorSintactico.class
│                   │   ├── Lexico.class
│                   │   ├── SiguienteToken.class
│                   │   ├── Sym.class
│                   │   └── Tokens.class
│                   └── inicio
│                       └── Inicio.class
├── sintacticov2.iml
└── src
    └── fes
        └── aragon
            ├── codigo
            │   ├── Axiomas.java
            │   ├── Derivador.java
            │   ├── ErrorSintactico.java
            │   ├── Lexico.java
            │   ├── SiguienteToken.java
            │   ├── Sym.java
            │   └── Tokens.java
            └── inicio
                └── Inicio.java
```
