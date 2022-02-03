# trading-local
This JAVA bot is for Algorithmic trading on IEX using Alpaca's API. The bot can be both serverless as an AWS lambda function or Azure function and can also
be run locally. 

## Strategies
The most developed strategy currently is the Mean Reversion Strategy, and infrastructure to back test this has been incorparted, thanks to ta4j. Other strategies include
bollinger bands, and the starting 15-min of the day strategy. 

## Currently working on
developing the bot to work with cryptocurrency, and porting some features to python for ease of development and better compatibility with broker's API. 
