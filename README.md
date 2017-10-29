# JIRA PDF


```bash
project = WTAI and assignee = kowolm and type not in (Sub-task, Epic) and status not in (Resolved, Closed)
```

## Default

```bash
gradle
```

## Build

```bash
gradle build
```

## Run

```bash
gradle run
```

## Tests

```bash
gradle check
```

## Continuous tests

```bash
gradle test -t
```

or

```bash
gradle test --continuous
```

## FatJar

```bash
gradle fatJar
java -jar build/libs/${NAME}-assembly-${VERSION}.jar
```

## Code coverage

```bash
gradle jacocoTestReport
open build/jacocoHtml/index.html
```

## Heroku

### Test on local

```bash
heroku local web
```

### Deploy

```bash
git push heroku master
```

## References

* http://itextpdf.com/
* http://developers.itextpdf.com/examples/itext5-building-blocks/rectangle-examples
