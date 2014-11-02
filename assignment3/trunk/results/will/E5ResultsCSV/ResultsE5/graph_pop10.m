% p10g100
p10g100 = csvread('FUN_NSGAII_10_10_pla33809.csv');

x = p10g100(:,1);
y = p10g100(:,2);
z = p10g100(:,3);

scatter3(x,y,z,'filled');
hold on;

% p10g1000

p10g1000 = csvread('FUN_NSGAII_10_100_pla33809.csv');

x = p10g1000(:,1);
y = p10g1000(:,2);
z = p10g1000(:,3);

scatter3(x,y,z,'filled')
hold on;

% p10g10000

p10g10000 = csvread('FUN_NSGAII_100_100_pla33809.csv');

x = p10g10000(:,1);
y = p10g10000(:,2);
z = p10g10000(:,3);

figure(1)

scatter3(x,y,z,'filled')

% set title
title('NSGAII on PLA33809')

% set axes
xlabel('Distance travelled')
ylabel('Remaining weight')
zlabel('Total profit')

% set legend
legend('show')
legend('pop10gen10','pop10gen100','pop100gen100')

%rotate the graph
view(140,15);

% save the graph
saveas(1, 'pla33809_nsga', 'png')