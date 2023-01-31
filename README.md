### Инструкция подключения БД и запуска SUT
1. Склонировать проект из репозитория командой git clone
2. Открыть склонированный проект в Intellij IDEA
3. Для запуска контейнеров с MySql, PostgreSQL и Node.js использовать команду docker-compose up -d --force-recreate
4. Запуск SUT

5. для MySQL ввести в терминале команду
 java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app  -jar artifacts/aqa-shop.jar

6. для PostgreSQL ввести в терминале команду
java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app  -jar artifacts/aqa-shop.jar

7. Запуск тестов (Allure)
8. для запуска на MySQL ввести команду
gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app allureReport

9. для запуска на PostgreSQ ввести команду
gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app allureReport

10. Открыть в Google Chrome сслыку http://localhost:8080
11. Для получения отчета Allure в браузере ввести команду gradlew allureServe
12. После окончания тестов завершить работу приложения (Ctrl+C), остановить контейнеры командой docker-compose down
