VERSION ?= dev

.PHONY: all ios android clean

all: ios android

ios:
	$(MAKE) -C ios VERSION=$(VERSION)

android:
	$(MAKE) -C android VERSION=$(VERSION)

clean:
	$(MAKE) -C ios clean
	$(MAKE) -C android clean
