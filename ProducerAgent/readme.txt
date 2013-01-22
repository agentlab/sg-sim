Агент CSAgent(система управления зданием) отсылает колличество потраченной энергии BOAgent'у(владелец здания). BOAgent делает запрос на цену всем брокерам(агент Broker) в системе, находит брокера с минимальной ценой и отправляет режим работы CSAgent'у.

Параметры запуска:
-gui
CSAgent:agents.CSAgent(100);BOAgent:agents.BOAgent;Broker1:agents.Broker(100,200);Broker2:agents.Broker(150,300)