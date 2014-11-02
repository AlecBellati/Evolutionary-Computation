% p100g100

p100g100 = csvread('pop100_gen100.csv');

x = p100g100(:,1);
y = p100g100(:,2);
z = p100g100(:,3);

scatter3(x,y,z,'filled')
hold on;

% p100g1000

p100g1000 = csvread('pop100_gen1000.csv');

x = p100g1000(:,1);
y = p100g1000(:,2);
z = p100g1000(:,3);

scatter3(x,y,z,'filled')
hold on;

% p100g10000

p100g10000 = csvread('pop100_gen10000.csv');

x = p100g10000(:,1);
y = p100g10000(:,2);
z = p100g10000(:,3);

figure(1);

scatter3(x,y,z,'filled');

% set title
title('a280 n1395 uncorr-similar-weights 05 - population: 100')

% set axes
xlabel('Distance travelled')
ylabel('Remaining weight')
zlabel('Total profit')

% set legend
legend('show')
legend('pop100gen100','pop100gen1000','pop100gen10000')

%rotate the graph
view(140,15);

% save the graph
saveas(1, 'pop100', 'png')