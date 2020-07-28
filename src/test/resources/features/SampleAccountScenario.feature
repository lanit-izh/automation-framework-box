# language: ru
# образец
@SampleAccountScenario

Функция: Sample

  Сценарий: Sample Account Scenario
    И сохранить данные УЗ пользователя:
      | Ключ          | Значение      |
      | user_name     | user_name     |
      | user_username | user_username |
      | user_password | user_password |

    И получить данные УЗ по имени пользователя "user_name"

    И обновить данные УЗ пользователя по столбцу "user_name" и значению "user_name":
      | Ключ          | Значение        |
      | user_name     | user_name_1     |
      | user_username | user_username_1 |
      | user_password | user_password_1 |

    И получить данные УЗ по имени пользователя "user_name_1"

    И сохранить данные УЗ пользователя:
      | Ключ          | Значение      |
      | user_name     | user_name     |
      | user_username | user_username |
      | user_password | user_password |

    И удалить данные УЗ пользователя по столбцу "user_name" и значению "user_name"

    И удалить все данные учетных записей

