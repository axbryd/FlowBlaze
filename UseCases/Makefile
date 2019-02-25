default:
	@python flow_blaze_banner.py
	@echo "Please use make firewall, make elephant,"
	@echo "make policer, make load-balancer or make clean"

firewall:
	@python flow_blaze_banner.py
	- rm -f *.json
	- rm -f *.xli
	- rm -f elfgen.h
	java -jar ../XL-toolchain/xlc.jar -i ../UseCases/port_knocking_firewall.xl -o ../UseCases/firewall.json
	@python flow_blaze_banner.py

elephant:
	@python flow_blaze_banner.py
	- rm -f *.json
	- rm -f *.xli
	- rm -f elfgen.h
	java -jar ../XL-toolchain/xlc.jar -i ../UseCases/elephant.xl -o ../UseCases/elephant.json
	@python flow_blaze_banner.py

policer:
	@python flow_blaze_banner.py
	- rm -f *.json
	- rm -f *.xli
	- rm -f elfgen.h
	java -jar ../XL-toolchain/xlc.jar -i ../UseCases/traffic_policer.xl -o ../UseCases/policer.json
	@python flow_blaze_banner.py

load-balancer:
	@python flow_blaze_banner.py
	- rm -f *.json
	- rm -f *.xli
	- rm -f elfgen.h
	java -jar ../XL-toolchain/xlc.jar -i ../UseCases/load_balancer.xl -o ../UseCases/load_balancer.json
	@python flow_blaze_banner.py

clean:
	@python flow_blaze_banner.py
	- rm -f *.json
	- rm -f *.xli
	- rm -f elfgen.h
