EchoServer
При запуске сервер начинает слушать заданный TCP-порт. При подключении клиента следует принять соединение и затем в цикле выполнять следующую последовательность:
- считать из сокета полученные данные
- отправить их обратно в неизменном виде
Продолжаем этот цикл до самостоятельного отключения клиента или получения команды "quit".
При получении команды "quit" сервер должен закрыть клиентский сокет.
