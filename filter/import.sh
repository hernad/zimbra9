for file in hernad@bring.out.ba
do
StrFilter=`cat "$file"`
Acc=`echo $file | cut -d "/" -f5`
su - zimbra -c "zmprov ma $Acc zimbraMailSieveScript '$StrFilter'"
echo "Process filter $Acc"
done
echo "All filter has been import successfully"
