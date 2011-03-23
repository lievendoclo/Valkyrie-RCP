dp.sh.Brushes.Text = function()
{
	this.regexList = [];

	this.CssClass = 'dp-txt';
	this.Style =	'.dp-txt .annotation { color: #646464; }' +
					'.dp-txt .number { color: #C00000; }';
}

dp.sh.Brushes.Text.prototype	= new dp.sh.Highlighter();
dp.sh.Brushes.Text.Aliases	= ['text'];