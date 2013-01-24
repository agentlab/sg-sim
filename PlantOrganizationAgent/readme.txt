Агент CSAgent(система управления заводом) отсылает колличество потраченной энергии POAgent'у(Заводу). POAgent делает запрос на цену всем брокерам(агент Broker) в системе, находит брокера с минимальной ценой и отправляет режим работы CSAgent'у.

Параметры запуска: 
-gui
CSAgent:agents.CSAgent(100);POAgent:agents.PlantOrganizationAgent;Broker1:agents.Broker(100,200);Broker2:agents.Broker(150,300)
