***

# Документация проекта FileDiff

## 1. Краткое описание проекта
**FileDiff** — это Java-приложение для отслеживания изменений в файловой системе. Оно позволяет создавать «снимки» (snapshots) структуры директорий и сравнивать их между собой, выявляя добавленные, удаленные и измененные файлы.

**Назначение:** Используется для контроля целостности файлов, отслеживания версий или анализа изменений в каталогах.

**Технологический стек:**
*   **Язык:** Java 11+
*   **Сборка:** Gradle
*   **Формат данных:** JSON (библиотека Jackson)
*   **Тестирование:** JUnit 5
*   **CI/CD:** GitHub Actions

---

## 2. Пример работы

### Сценарий:
1.  Пользователь сканирует папку `src/`. Приложение генерирует JSON-файл с хэшами файлов.
2.  Пользователь меняет код в одном файле и добавляет новый.
3.  Пользователь снова сканирует папку `src/` во второй JSON-файл.
4.  Приложение сравнивает два JSON-файла.

### Входные данные (режим Diff):
*   `snapshot_v1.json` (старое состояние)
*   `snapshot_v2.json` (новое состояние)

### Выходные данные (stdout):
```text
CHANGED: /app/src/main/Main.java
ADDED: /app/src/utils/NewUtil.java
REMOVED: /app/src/temp/old_config.xml
```

---

## 3. Сборка проекта

Для сборки проекта необходимо наличие установленного JDK (версии 11 или выше). Gradle устанавливать не нужно, используется Gradle Wrapper.

**Linux / macOS:**
```bash
./gradlew build
```

**Windows (PowerShell / CMD):**
```cmd
gradlew.bat build
```

После успешной сборки исполняемый jar-файл появится по пути:
`build/libs/filediff-1.0-SNAPSHOT.jar`

---

## 4. Конфигурация проекта

Перед запуском необходимо создать файл конфигурации `app.properties` в той же директории, откуда вы будете запускать приложение.

**Пример содержимого `app.properties`:**

```properties
# Выбор алгоритма хэширования.
# Доступные варианты: MD5, SHA-1, SHA-256
hash.algorithm=MD5

# Список расширений файлов, которые нужно игнорировать при сканировании и сравнении.
# Указываются через запятую без пробелов.
scan.ignore.patterns=*.log,*.tmp,*.bak,*.idea
```

---

## 5. Запуск тестов

Проект покрыт модульными тестами (JUnit 5).

**Команда для запуска:**
```bash
./gradlew test
```

**Ожидаемый результат:**
В консоли должно появиться сообщение `BUILD SUCCESSFUL`.
Подробный отчет о прохождении тестов генерируется в файле:
`build/reports/tests/test/index.html` (можно открыть в браузере).

---

## 6. Запуск приложения

Приложение работает в двух режимах: `scan` и `diff`. Параметры передаются через **переменные окружения** (Environment Variables).

### Режим 1: Сканирование (Scan)
Рекурсивно обходит директорию, считает хэши и сохраняет результат в JSON.

Необходимые переменные:
*   `SCAN_PATH`: Путь к сканируемой директории.
*   `SNAPSHOT_OUTPUT`: Путь к выходному файлу JSON.

**Пример запуска (Linux/macOS):**
```bash
export SCAN_PATH="./src"
export SNAPSHOT_OUTPUT="./snapshot_v1.json"
java -jar build/libs/filediff-1.0-SNAPSHOT.jar scan
```

**Пример запуска (Windows PowerShell):**
```powershell
$env:SCAN_PATH=".\src"
$env:SNAPSHOT_OUTPUT=".\snapshot_v1.json"
java -jar build\libs\filediff-1.0-SNAPSHOT.jar scan
```

### Режим 2: Сравнение (Diff)
Сравнивает два файла snapshot и выводит разницу.

Необходимые переменные:
*   `SNAPSHOT_OLD`: Путь к старому снимку.
*   `SNAPSHOT_NEW`: Путь к новому снимку.

**Пример запуска (Linux/macOS):**
```bash
export SNAPSHOT_OLD="./snapshot_v1.json"
export SNAPSHOT_NEW="./snapshot_v2.json"
java -jar build/libs/filediff-1.0-SNAPSHOT.jar diff
```

**Пример запуска (Windows PowerShell):**
```powershell
$env:SNAPSHOT_OLD=".\snapshot_v1.json"
$env:SNAPSHOT_NEW=".\snapshot_v2.json"
java -jar build\libs\filediff-1.0-SNAPSHOT.jar diff
```

---
**Автор:** [Рудинский Дмитрий]