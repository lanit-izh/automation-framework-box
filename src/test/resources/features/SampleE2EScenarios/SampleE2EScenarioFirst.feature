# language: ru
# образец e2e
@SampleE2EScenarioFirst

Функция: Sample E2E

  # Первому сценарию в цепочке необходимо передать alias_output на запись сохраненных значений
  Сценарий: SampleE2EScenarioFirst
    Дано открываем тестируемое приложение
    И перейти к странице 'Поиск яндекс'
    Затем на текущей странице перейти к блоку "Блок поиска"
    И ввести в поле ввода значение "ДатаПровайдер!(valueee,тест поиск)"
    Затем нажать на кнопку с текстом "Найти"
    И перейти к странице 'Результаты поиска'
    Затем на текущей странице перейти к блоку "Результат поисковой выдачи" - "1"
    И сохранить ссылку для e2e теста с ключом "link"


