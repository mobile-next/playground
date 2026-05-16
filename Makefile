.PHONY: all ios android clean

all: ios android

ios:
	$(MAKE) -C ios

android:
	$(MAKE) -C android

clean:
	$(MAKE) -C ios clean
	$(MAKE) -C android clean
