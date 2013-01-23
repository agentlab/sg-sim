Агент CSAgent(система управления установкой производства электроэнергии) отсылает количество энергии ProducerAgent'у(Производителю). Producer Agent делает запрос на цену всем брокерам(агент Broker) в системе, находит брокера с лучшей ценой и отправляет режим работы CSAgent'у.


Параметры запуска:
-gui
CSAgent:agents.CSAgent(100);ProducerAgent:agents.ProducerAgent;Broker1:agents.Broker(100,200);Broker2:agents.Broker(150,300)