Этот репозиторий содержит серверную часть веб-приложения для хранения складских запасов книжного магазина

# Технологии
Язык программирования: Java

Фреймворк: Java Spring Boot

СУБД: PostgreSQL

# Конечные точки API
- Аутентификация
  - `POST`      `/api/login` - Вход пользователя.
  - `POST`      `/api/register` - Регистрация пользователя.
  - `POST`      `/api/logout` - Выход пользователя
- Категории
  - `GET`       `/api/category` - Получить все категории пользователя.
  - `POST`      `/api/category` - Добавить новую категорию
  - `GET`       `/api/category/{id}` - Получить категорию по ID.
  - `PATCH`     `/api/category/{id}/edit` - Обновить информацию о категории.
  - `DELETE`    `/api/category/{id}` - Удалить категорию.
- Товары(Книги)
  - `GET`       `/api/inventory` - Получить все книги.
  - `GET`       `/api/inventory/{id}` - Получить книгу по ID.
  - `POST`      `/api/inventory` - Добавить новую книгу.
  - `PATCH`    `/api/inventory/{id}/edit` - Обновить книгу.
- Заказы
  - `GET`       `/api/orders` - ППолучить все заказы с товарами.
  - `GET`       `/api/orders/{id}` - Получить детали заказа.
  - `POST`      `/api/orders` - Создать новый заказ.
  - `PATCH`     `/api/orders/{id}/status` - Обновить статус заказа.
- Метрики
  - `GET`       `/api/metrics/current-stock` - Текущие запасы.
  - `GET`       `/api/metrics/order-status` - Статусы заказов.
  - `GET`       `/api/metrics/order-count` -  Статистика заказов

# Установка и запуск
## 1 Клонируйте репозиторий
```
git clone https://github.com/Smileface22/Coursework_RKSP.git
```
## 2 Перейдите в папку проекта
```
cd Coursework_PKSP
```
## 3 Создайте .env по примеру .env.example
## 4 Сборка проекта
```
gradle build
```
## 5 Запустите приложение
```
gradle bootRun
```
Приложение будет доступно по адресу: http://localhost:8080
