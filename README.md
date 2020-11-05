# Jilb metrics
Лабораторная работа по МСиСвИнфТ, БГУИР, 2020, Минск
## Использование
1. Инициализируем метрику:
```Java
JilbMetrics j = new JilbMetrics(rawText); //program raw text
```
2. Достаём метрики:
```Java
j.getAbsoluteDifficulty();
j.getRelativeDifficulty();
j.getMaxNestingLevel();
```
3. (опционально) Достаём количество операторов:
```Java
j.getTotalOperatorAmount();
```
