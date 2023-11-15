out-dir := out
uml-src-dir := uml/src
gv-src-dir := graphviz
uml-include-dir := uml/include
uml-out-dir := $(out-dir)/uml
gv-out-dir := $(out-dir)/graphviz

uml-srcs := $(wildcard $(uml-src-dir)/**/*.puml $(uml-src-dir)/*.puml)
uml-imgs := $(uml-srcs:$(uml-src-dir)/%.puml=$(uml-out-dir)/%.png)

gv-srcs := $(wildcard $(gv-src-dir)/**/*.dot $(gv-src-dir)/*.dot)
gv-imgs := $(gv-srcs:$(gv-src-dir)/%.dot=$(gv-out-dir)/%.png)

dossier-srcs := rendu.md
dossier-pdfs := $(dossier-srcs:%.md=$(out-dir)/%.pdf)

pandoc-args := --toc --toc-depth=2 --listings --template eisvogel

 $(info $(dossier-pdfs))

.PHONY = all pdf uml

all: pdf uml graphviz
uml: $(uml-imgs)
graphviz: $(gv-imgs)

pdf: uml $(dossier-pdfs)

$(uml-out-dir)/%.png: $(uml-src-dir)/%.puml
	plantuml -o $(abspath $(@D)) $<

$(gv-out-dir)/%.png: $(gv-src-dir)/%.dot | $(gv-out-dir)
	dot -T png -o $@ $<

$(gv-out-dir):
	mkdir -p $(gv-out-dir)

$(out-dir)/%.pdf: %.md $(uml-imgs) $(gv-imgs)
	docker run --rm \
		   --volume "`pwd`:/data" \
		   --user $(id -u):$(id -g) \
		   pandoc/extra $< -o $@ $(pandoc-args)
	
