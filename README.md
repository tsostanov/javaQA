# Лабораторная работа №2: система функций

Проект на `Java 17` и `Maven` для лабораторной работы по ТПО.

Базовые функции:

- базовая тригонометрическая функция: `cos(x)`
- базовая логарифмическая функция: `ln(x)`

Все остальные тригонометрические и логарифмические функции выражаются через них.

## Структура проекта

- `src/main/java/ru/ifmo/se/lab2/modules/trig` - тригонометрические модули: `cos`, `sin`, `sec`, `csc`, `cot`
- `src/main/java/ru/ifmo/se/lab2/modules/logs` - логарифмические модули: `ln`, `log2`, `log3`, `log5`, `log10`
- `src/main/java/ru/ifmo/se/lab2/modules/system` - модуль системы функций
- `src/main/java/ru/ifmo/se/lab2/io` - экспорт значений в `CSV`
- `src/test/java/ru/ifmo/se/lab2/stubs` - табличные заглушки для интеграционных тестов
- `src/test/java/ru/ifmo/se/lab2/mocks` - проверяемые mock-объекты
- `src/test/java/ru/ifmo/se/lab2/spies` - spy-объекты для отслеживания вызовов

## Система функций

Для `x <= 0`:

```text
((((((csc(x) / csc(x)) / sec(x)) - sin(x)) + cot(x))^3 + (cos(x) + csc(x)) - (sec(x) - sin(x)))
/ (((cot(x)^2) / (csc(x) + sec(x))) * sin(x)^2))^2 / cot(x)
```

Для `x > 0`:

```text
(log10(x) * log10(x) * log2(x)) + log3(x) + log5(x) + (ln(x)^3)
```

## Сборка проекта

Сборка без запуска тестов:

```powershell
mvn "-Dmaven.test.skip=true" package
```

Итоговый jar-файл:

```text
target/tpojava-1.0-SNAPSHOT.jar
```

## Запуск приложения

Пример запуска:

```powershell
java -jar target\tpojava-1.0-SNAPSHOT.jar system -2 2 0.1 result.csv 1E-8
```

Аргументы командной строки:

- имя модуля
- начальное значение `x`
- конечное значение `x`
- шаг
- путь к `csv`-файлу, необязательно
- `epsilon`, необязательно

Доступные модули:

```text
system, cos, sin, sec, csc, cot, ln, log2, log3, log5, log10
```

## Тестирование

Запуск тестов:

```powershell
mvn test
```

Запуск тестов с отчетом о покрытии:

```powershell
mvn clean verify
```

Отчет JaCoCo:

```text
target/site/jacoco/index.html
```

В проекте также включена проверка минимального покрытия кода.

## Интеграционное тестирование

В проекте есть:

- модульные тесты для базовых и производных функций
- интеграционные тесты для тригонометрических модулей
- интеграционные тесты для логарифмических модулей
- интеграционные тесты для полной системы функций

Используемые test doubles:

- `stubs`:
  `TableMathModule`, `ModuleStubFactory`
- `mocks`:
  `VerifiableMathModuleMock`
- `spies`:
  `TrackingMathModuleSpy`
