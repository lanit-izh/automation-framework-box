# language: ru
# образец
@Api

Функция: Sample

  Сценарий:  SampleScenarioApi

    И сохранить в переменные цитруса по ключу "Отправитель" значение "Lanit_Izh"
    И сохранить в переменные цитруса по ключу "Получатель" значение "world"
    И сохранить в переменные цитруса по ключу "Имя сообщения" значение "citrus:randomNumber(10)"
    И версия сервиса должна быть "Version 2.1.1 FOAAS"
    И отправить в сервис запрос  "/cool/:${Отправитель}" и получить ответ "Cool story, bro. - :${Отправитель}"
    И отправить в сервис запрос  "/xmas/:${Получатель}/:${Отправитель}" и получить ответ "Merry Fucking Christmas, :${Получатель}. - :${Отправитель}"
    И отправить сообщение с именем "Merry Fucking Christmas" в сервис запрос "/xmas/:${Получатель}/:${Отправитель}" и получить ответ "Merry Fucking Christmas, :${Получатель}. - :${Отправитель}"
    Тогда проверить что существует сообщение с именем "Merry Fucking Christmas"
    И отправляю соап сообщение