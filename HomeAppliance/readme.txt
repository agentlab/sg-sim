������ �������:
	������� �����	��3-112
	�������� ����	��3-112

�������� ������:
	ConditionerAgent - ����� �����������
	CookerAgent - ����� �����
	
��������������� ������:
	ControlAgent - �����, ����������� ������ � �������������. ��������� ������ ������ "�������"
	SubscriberAgent - �����, ���������� ���������� �� ����� � ������������. 
					  ��������� ������ ������ "�������" � ������ "�����������������"
					  
��������������� ������ ������������� �� �������� ���������� �� �������� ������� (�������� Subscription).
�������� ������ ��������� �������� � ������������ ��� �������� � ������� ��������� ������ (�����������, ����������� ����������� � �.�.)

��������� ��������� ������ ��� ������� �������:
-gui
conditioner:agents.ConditionerAgent;cooker:agents.CookerAgent;energy_station_conditioner:agents.SubscriberAgent(0,0);granny_look_conditioner:agents.SubscriberAgent(0,1);energy_station_cooker:agents.SubscriberAgent(1,0);granny_look_cooker:agents.SubscriberAgent(1,1);granny_control_conditioner:agents.ControlAgent(conditioner);;granny_control_cooker:agents.ControlAgent(cooker)

conditioner - ����� �����������
cooker - ����� �����
energy_station_conditioner - �������� ������ �����������������, �������� ���������� � ������� ������������������
granny_look_conditioner - �������� ������ �������, �������� ���������� �� ����������� ����������� ������������
energy_station_cooker - �������� ������ �����������������, �������� ���������� � ������� ������������������
granny_look_cooker - �������� ������ �������, �������� ���������� � ����������� �����
granny_control_conditioner - �������� ������ �������, ��������� ������������ � ��������� ������� ������������
granny_control_cooker - �������� ������ �������, ��������� ������������ �����
