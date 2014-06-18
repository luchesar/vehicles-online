function AutoFillTodaysDate(day, month, year) {

	var dod_elm = document.getElementById("todaysDateOfDisposalLabel");
	var dod_day = document.getElementById("dateOfDisposal_day");
	var dod_month = document.getElementById("dateOfDisposal_month");
	var dod_year = document.getElementById("dateOfDisposal_year");

  if (dod_elm.checked === false) {
    dod_day.value = '';
    dod_month.value = '';
    dod_year.value = '';
  } else {
    dod_day.value = day;
    dod_month.value = month;
    dod_year.value = year;
  }
}