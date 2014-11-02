% p10g100
p10g100 = csvread('pop10_gen100.csv');

x = p10g100(:,1);
y = p10g100(:,2);
z = p10g100(:,3);

scatter3(x,y,z,'filled');
hold on;

% p10g1000

p10g1000 = csvread('pop10_gen1000.csv');

x = p10g1000(:,1);
y = p10g1000(:,2);
z = p10g1000(:,3);

scatter3(x,y,z,'filled')
hold on;

% p10g10000

p10g10000 = csvread('pop10_gen10000.csv');

x = p10g10000(:,1);
y = p10g10000(:,2);
z = p10g10000(:,3);

figure(1)

scatter3(x,y,z,'filled')

% set title
title('a280 n279 bounded-strongly-corr 01 - population: 10')

% set axes
xlabel('Distance travelled')
ylabel('Remaining weight')
zlabel('Total profit')

% set legend
legend('show')
legend('pop10gen100','pop10gen1000','pop10gen10000')

%rotate the graph
view(140,15);

% save the graph
saveas(1, 'pop10', 'png')