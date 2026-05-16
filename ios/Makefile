SCHEME    = playground
PROJECT   = playground.xcodeproj
SDK       = iphonesimulator
BUILD_DIR = build
VERSION  ?= dev
ZIP_NAME  = Playground-$(VERSION).zip

.PHONY: all clean

all: $(ZIP_NAME)

$(ZIP_NAME):
	xcodebuild \
		-project $(PROJECT) \
		-scheme $(SCHEME) \
		-sdk $(SDK) \
		-configuration Debug \
		-derivedDataPath $(BUILD_DIR) \
		build
	cd $(BUILD_DIR)/Build/Products/Debug-iphonesimulator && zip -r $(CURDIR)/$(ZIP_NAME) $(SCHEME).app

clean:
	rm -rf $(BUILD_DIR) Playground-*.zip
