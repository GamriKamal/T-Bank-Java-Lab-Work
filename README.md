# T-Bank Translator

## Описание проекта

Данный проект представляет собой выполнение лабораторной работы по Java для курса "Java-разработчик, осень 2024" от T-Банка.

## Пререквизиты

* Java 17
* Maven
* Spring Boot 3
* PostgreSQL
* Docker

## Использование

Для начала работы с проектом, потребуется получить API ключ для переводчика на сайте [Yandex Cloud](https://cloud.yandex.ru).

<details>
<summary><strong>Как получить ключ?</strong></summary>
### Как получить ключ?

1. Пройдите аутентификацию через Yandex ID.
2. После входа у вас создастся облако и дефолтный каталог. Скопируйте ID вашего каталога и сохраните его в удобное место — он вам понадобится.
   ![Скриншот меню](images/folder_id.png)
4. Перейдите во вкладку “Сервисные аккаунты”, нажмите на троеточие слева и выберите “Создать сервисный аккаунт”.
   ![Скриншот меню](images/service_accounts.png)
   ![Скриншот меню](images/creation_service_account.png)
   ![Скриншот меню](images/creation_service_account_.png)
    * Заполните данные и в поле "роль" выберите `ai.translate.user` (это и будет роль переводчика Яндекса), затем нажмите "Создать".
    ![Скриншот меню](images/menu_creation_service_account.png)
6. Создайте платежный аккаунт для использования сервисов Yandex Cloud. (Если у вас его нет)
    * Заполните информацию в соответствии с вашими данными. Yandex Cloud является бесплатным при соблюдении квот и лимитов, подробнее об этом можно почитать [здесь](https://cloud.yandex.ru/docs/billing/overview).
   ![Скриншот меню](images/menu_pay_account.png)

8. После создания сервисного аккаунта, перейдите на его страницу.
   ![Скриншот меню](images/press_service_account.png)
   ![Скриншот меню](images/menu_service_account.png)
10. Сгенерируйте API ключ:
    * Нажмите "Создать новый ключ" и выберите "Создать API-ключ".
    ![Скриншот меню](images/menu_service_account_api.png)
    ![Скриншот меню](images/menu_service_account_create_api.png)
    * Сохраните идентификатор и ключ в удобное место. После закрытия окна ключ будет недоступен для просмотра.
    ![Скриншот меню](images/result_api.png)
Теперь вы готовы использовать API ключ для работы с проектом.

</details>

## Настройка API ключа и Folder ID:

   Перейдите в файл application.properties в папке src/main/resources и измените значения для folderID и api-key:
   ```bash
   yandex.translate.folder-id=ВАШ_FOLDER_ID
   yandex.translate.api-key=ВАШ_API_KEY
   ```
## Запуск программы

Для удобства работы с Docker был написан `Makefile`, который содержит команды для управления Docker-контейнерами. Для запуска проекта и работы с Docker-контейнерами выполните следующие шаги:

1. **Сборка и установка проекта:**

   Выполните следующую команду для сборки проекта, установки зависимостей и создания Docker-образов:

   ```bash
   make launch

2. **Запуск контейнеров:**

   Для запуска Docker-контейнеров используйте команду:
   ```bash
   make up

3. **Запуск сгенерированного Swagger UI:**

После запуска контейнеров вы можете получить доступ к Swagger UI, который предоставляет интерфейс для взаимодействия с API. Swagger UI доступен по следующему URL: http://localhost:8080/swagger-ui.html.
