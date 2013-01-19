Авторы проекта:
	Алешина Лилия	ИУ3-112
	Карманов Иван	ИУ3-112

Основные агенты:
	ConditionerAgent - агент кондиционер
	CookerAgent - агент плита
	
Вспомогательные агенты:
	ControlAgent - агент, управляющий плитой и кондиционером. Эмелирует работу агента "бабушка"
	SubscriberAgent - агент, получающий информацию от плиты и кондиционера. 
					  Эмулирует работу агента "бабушка" и агента "электроподстанция"
					  
Вспомогательные агенты подписываются на рассылку информации от основных агентов (протокол Subscription).
Основные агенты рассылают информаю о потребляемой ими мощности и текущем состоянии работы (температура, охлаждающая способность и т.п.)

Аргументы командной строки для запуска агентов:
-gui
conditioner:agents.ConditionerAgent;cooker:agents.CookerAgent;energy_station_conditioner:agents.SubscriberAgent(0,0);granny_look_conditioner:agents.SubscriberAgent(0,1);energy_station_cooker:agents.SubscriberAgent(1,0);granny_look_cooker:agents.SubscriberAgent(1,1);granny_control_conditioner:agents.ControlAgent(conditioner);;granny_control_cooker:agents.ControlAgent(cooker)

conditioner - агент кондиционер
cooker - агент плита
energy_station_conditioner - эмулятор агента электроподстанция, получает информацию о текущем электропотреблении
granny_look_conditioner - эмулятор агента бабушка, получает информацию об охлаждающей способности кондиционера
energy_station_cooker - эмулятор агента электроподстанция, получает информацию о текущем электропотреблении
granny_look_cooker - эмулятор агента бабушка, получает информацию о температуре плиты
granny_control_conditioner - эмулятор агента бабушка, управляет температурой и воздушным потоком кондиционера
granny_control_cooker - эмулятор агента бабушка, управляет температурой плиты
