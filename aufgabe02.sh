# Aufgabe: Uhrzeit und Datum in eine Datei schreiben

echo "Heute ist der $(date +'%d.%m.%Y')" > Datei.txt
echo "Es ist $(date +'%H:%M') Uhr" >> Datei.txt

# Aufgabe: Größten Platzfresser finden
du | sort -h
